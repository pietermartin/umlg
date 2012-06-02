package org.tuml.javageneration.visitor.enumeration;

import org.opaeum.java.metamodel.OJPackage;
import org.opaeum.java.metamodel.annotation.OJEnum;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.naming.Namer;
import org.tuml.javageneration.visitor.BaseVisitor;

public class EnumerationVisitor extends BaseVisitor implements Visitor<org.eclipse.uml2.uml.Enumeration> {

	public void visitBefore(org.eclipse.uml2.uml.Enumeration enumeration) {
		OJEnum ojEnum = new OJEnum(enumeration.getName());
		OJPackage ojPackage = new OJPackage(Namer.name(enumeration.getNearestPackage()));
		ojEnum.setMyPackage(ojPackage);
		addToSource(ojEnum);
	}

	public void visitAfter(org.eclipse.uml2.uml.Enumeration enumeration) {
		
	}

}
