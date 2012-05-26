package org.tuml.javageneration.visitor.interfaze;

import org.opaeum.java.metamodel.OJPackage;
import org.opaeum.java.metamodel.annotation.OJAnnotatedInterface;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.naming.Namer;
import org.tuml.javageneration.visitor.BaseVisitor;

public class InterfaceVisitor extends BaseVisitor implements Visitor<org.eclipse.uml2.uml.Interface> {

	public void visitBefore(org.eclipse.uml2.uml.Interface clazz) {
		OJAnnotatedInterface annotatedInterface = new OJAnnotatedInterface(Namer.name(clazz));
		OJPackage ojPackage = new OJPackage(Namer.name(clazz.getNearestPackage()));
		annotatedInterface.setMyPackage(ojPackage);
		addToSource(annotatedInterface);
	}

	public void visitAfter(org.eclipse.uml2.uml.Interface clazz) {
		
	}

}
