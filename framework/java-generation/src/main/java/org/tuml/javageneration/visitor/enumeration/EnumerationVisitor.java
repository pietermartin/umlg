package org.tuml.javageneration.visitor.enumeration;

import org.eclipse.uml2.uml.EnumerationLiteral;
import org.opaeum.java.metamodel.OJPackage;
import org.opaeum.java.metamodel.annotation.OJEnum;
import org.opaeum.java.metamodel.annotation.OJEnumLiteral;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.Namer;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.visitor.BaseVisitor;
import org.tuml.javageneration.visitor.clazz.ClassBuilder;

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
