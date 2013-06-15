package org.umlg.javageneration.visitor.enumeration;

import org.eclipse.uml2.uml.EnumerationLiteral;
import org.umlg.java.metamodel.OJPackage;
import org.umlg.java.metamodel.annotation.OJEnum;
import org.umlg.java.metamodel.annotation.OJEnumLiteral;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.util.Namer;
import org.umlg.javageneration.util.TinkerGenerationUtil;
import org.umlg.javageneration.visitor.BaseVisitor;
import org.umlg.javageneration.visitor.clazz.ClassBuilder;

public class EnumerationVisitor extends BaseVisitor implements Visitor<org.eclipse.uml2.uml.Enumeration> {

	public EnumerationVisitor(Workspace workspace) {
		super(workspace);
	}

	public void visitBefore(org.eclipse.uml2.uml.Enumeration enumeration) {
		OJEnum ojEnum = new OJEnum(enumeration.getName());
		ojEnum.addToImplementedInterfaces(TinkerGenerationUtil.TumlEnum);
		OJPackage ojPackage = new OJPackage(Namer.name(enumeration.getNearestPackage()));
		ojEnum.setMyPackage(ojPackage);
		for (EnumerationLiteral literal : enumeration.getOwnedLiterals()) {
			OJEnumLiteral ojLiteral = new OJEnumLiteral( literal.getName());
			ojEnum.addToLiterals(ojLiteral);
		}
		addToSource(ojEnum);
		ClassBuilder.addGetQualifiedName(ojEnum, enumeration);
	}

	public void visitAfter(org.eclipse.uml2.uml.Enumeration enumeration) {
		
	}

}
