package org.tuml.javageneration.visitors;

import org.opaeum.java.metamodel.OJPackage;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.naming.Namer;

public class ClassifierVisitor extends BaseVisitor implements Visitor<org.eclipse.uml2.uml.Class> {

	public void visitBefore(org.eclipse.uml2.uml.Class clazz) {
		OJAnnotatedClass annotatedClass = new OJAnnotatedClass(Namer.name(clazz));
		OJPackage ojPackage = new OJPackage(Namer.name(clazz.getNearestPackage()));
		annotatedClass.setMyPackage(ojPackage);
		addToSource(annotatedClass);
	}

	public void visitAfter(org.eclipse.uml2.uml.Class clazz) {
		
	}

}
