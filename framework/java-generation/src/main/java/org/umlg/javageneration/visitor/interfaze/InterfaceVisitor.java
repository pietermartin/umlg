package org.umlg.javageneration.visitor.interfaze;

import org.eclipse.uml2.uml.Classifier;
import org.umlg.java.metamodel.OJPackage;
import org.umlg.java.metamodel.annotation.OJAnnotatedInterface;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.Namer;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.util.UmlgInterfaceOperations;
import org.umlg.javageneration.visitor.BaseVisitor;

public class InterfaceVisitor extends BaseVisitor implements Visitor<org.eclipse.uml2.uml.Interface> {

	public InterfaceVisitor(Workspace workspace) {
		super(workspace);
	}

	public void visitBefore(org.eclipse.uml2.uml.Interface inf) {
		OJAnnotatedInterface annotatedInterface = new OJAnnotatedInterface(Namer.name(inf));
		OJPackage ojPackage = new OJPackage(Namer.name(inf.getNearestPackage()));
		annotatedInterface.setMyPackage(ojPackage);
		if (UmlgInterfaceOperations.hasCompositeOwner(inf)) {
			extendCompositionNode(annotatedInterface);
		} else {
			extendTumlNode(annotatedInterface);
		}
		
		for (Classifier c : inf.getGenerals()) {
			annotatedInterface.addToSuperInterfaces(UmlgClassOperations.getPathName(c));
		}
		
		addToSource(annotatedInterface);
	}

	public void visitAfter(org.eclipse.uml2.uml.Interface clazz) {
		
	}

	private void extendCompositionNode(OJAnnotatedInterface annotatedInterface) {
		annotatedInterface.addToSuperInterfaces(UmlgGenerationUtil.umlgCompositionNodePathName);
	}
	
	private void extendTumlNode(OJAnnotatedInterface annotatedInterface) {
		annotatedInterface.addToSuperInterfaces(UmlgGenerationUtil.UMLG_NODE);
	}
}
