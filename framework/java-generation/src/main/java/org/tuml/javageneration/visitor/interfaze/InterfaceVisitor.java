package org.tuml.javageneration.visitor.interfaze;

import org.eclipse.uml2.uml.Classifier;
import org.opaeum.java.metamodel.OJPackage;
import org.opaeum.java.metamodel.annotation.OJAnnotatedInterface;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.Namer;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.util.TumlInterfaceOperations;
import org.tuml.javageneration.visitor.BaseVisitor;

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
		annotatedInterface.addToSuperInterfaces(TinkerGenerationUtil.TINKER_NODE);
	}
}
