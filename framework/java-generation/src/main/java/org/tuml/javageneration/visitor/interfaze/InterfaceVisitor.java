package org.tuml.javageneration.visitor.interfaze;

import org.opaeum.java.metamodel.OJPackage;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedInterface;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.naming.Namer;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlInterfaceOperations;
import org.tuml.javageneration.visitor.BaseVisitor;

public class InterfaceVisitor extends BaseVisitor implements Visitor<org.eclipse.uml2.uml.Interface> {

	public void visitBefore(org.eclipse.uml2.uml.Interface inf) {
		OJAnnotatedInterface annotatedInterface = new OJAnnotatedInterface(Namer.name(inf));
		OJPackage ojPackage = new OJPackage(Namer.name(inf.getNearestPackage()));
		annotatedInterface.setMyPackage(ojPackage);
		if (TumlInterfaceOperations.hasCompositeOwner(inf)) {
			implementCompositionNode(annotatedInterface);
		} else {
			implementTumlNode(annotatedInterface);
		}
		addToSource(annotatedInterface);
	}

	public void visitAfter(org.eclipse.uml2.uml.Interface clazz) {
		
	}

	private void implementCompositionNode(OJAnnotatedInterface annotatedInterface) {
		annotatedInterface.addToSuperInterfaces(TinkerGenerationUtil.tinkerCompositionNodePathName);
	}
	
	private void implementTumlNode(OJAnnotatedInterface annotatedInterface) {
		annotatedInterface.addToSuperInterfaces(TinkerGenerationUtil.TINKER_NODE);
	}
}
