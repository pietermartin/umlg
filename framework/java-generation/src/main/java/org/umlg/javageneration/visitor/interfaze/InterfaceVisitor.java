package org.umlg.javageneration.visitor.interfaze;

import org.eclipse.uml2.uml.Classifier;
import org.umlg.java.metamodel.OJPackage;
import org.umlg.java.metamodel.annotation.OJAnnotatedInterface;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.Namer;
import org.umlg.javageneration.util.TinkerGenerationUtil;
import org.umlg.javageneration.util.TumlClassOperations;
import org.umlg.javageneration.util.TumlInterfaceOperations;
import org.umlg.javageneration.visitor.BaseVisitor;

public class InterfaceVisitor extends BaseVisitor implements Visitor<org.eclipse.uml2.uml.Interface> {

	public InterfaceVisitor(Workspace workspace) {
		super(workspace);
	}

	public void visitBefore(org.eclipse.uml2.uml.Interface inf) {
		OJAnnotatedInterface annotatedInterface = new OJAnnotatedInterface(Namer.name(inf));
		OJPackage ojPackage = new OJPackage(Namer.name(inf.getNearestPackage()));
		annotatedInterface.setMyPackage(ojPackage);
		if (TumlInterfaceOperations.hasCompositeOwner(inf)) {
			extendCompositionNode(annotatedInterface);
		} else {
			extendTumlNode(annotatedInterface);
		}
		
		for (Classifier c : inf.getGenerals()) {
			annotatedInterface.addToSuperInterfaces(TumlClassOperations.getPathName(c));
		}
		
		addToSource(annotatedInterface);
	}

	public void visitAfter(org.eclipse.uml2.uml.Interface clazz) {
		
	}

	private void extendCompositionNode(OJAnnotatedInterface annotatedInterface) {
		annotatedInterface.addToSuperInterfaces(TinkerGenerationUtil.tinkerCompositionNodePathName);
	}
	
	private void extendTumlNode(OJAnnotatedInterface annotatedInterface) {
		annotatedInterface.addToSuperInterfaces(TinkerGenerationUtil.TUML_NODE);
	}
}
