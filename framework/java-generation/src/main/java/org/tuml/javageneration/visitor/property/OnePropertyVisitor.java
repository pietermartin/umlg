package org.tuml.javageneration.visitor.property;

import org.eclipse.uml2.uml.Property;
import org.tuml.java.metamodel.OJField;
import org.tuml.java.metamodel.OJIfStatement;
import org.tuml.java.metamodel.OJPathName;
import org.tuml.java.metamodel.OJVisibilityKind;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.java.metamodel.annotation.OJAnnotatedField;
import org.tuml.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.util.TumlClassOperations;
import org.tuml.javageneration.visitor.BaseVisitor;

public class OnePropertyVisitor extends BaseVisitor implements Visitor<Property> {

	public OnePropertyVisitor(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Property p) {
		PropertyWrapper propertyWrapper = new PropertyWrapper(p);
		// TODO qualifiers
		if (propertyWrapper.isOne() && !propertyWrapper.isDerived() && !propertyWrapper.isQualifier()) {
			OJAnnotatedClass owner = findOJClass(p);
			buildGetter(owner, propertyWrapper);
			buildOneAdder(owner, propertyWrapper);
			buildSetter(owner, propertyWrapper);
		}
	}

	@Override
	public void visitAfter(Property element) {

	}

	/*
	 * ToOne properties are stored in a List similar to toMany. The first
	 * element is returned
	 */
	public static void buildGetter(OJAnnotatedClass owner, PropertyWrapper propertyWrapper) {
		OJAnnotatedOperation getter = new OJAnnotatedOperation(propertyWrapper.getter(), propertyWrapper.javaBaseTypePath());
		OJAnnotatedField tmpField = new OJAnnotatedField("tmp", propertyWrapper.javaTumlTypePath());
		getter.getBody().addToLocals(tmpField);
		tmpField.setInitExp("this." + propertyWrapper.fieldname());
		OJIfStatement ifFieldNotEmpty = new OJIfStatement("!" + tmpField.getName() + ".isEmpty()");
		if (propertyWrapper.isOrdered()) {
			ifFieldNotEmpty.addToThenPart("return " + tmpField.getName() + ".get(0)");
		} else {
			ifFieldNotEmpty.addToThenPart("return " + tmpField.getName() + ".iterator().next()");
		}
		ifFieldNotEmpty.addToElsePart("return null");
		getter.getBody().addToStatements(ifFieldNotEmpty);
		owner.addToOperations(getter);
	}

	public static void buildOneAdder(OJAnnotatedClass owner, PropertyWrapper propertyWrapper) {
		OJAnnotatedOperation singleAdder = new OJAnnotatedOperation(propertyWrapper.adder());
		singleAdder.addParam(propertyWrapper.fieldname(), propertyWrapper.javaBaseTypePath());
		if (!propertyWrapper.isDataType()) {
			OJIfStatement ifNotNull = new OJIfStatement(propertyWrapper.fieldname() + " != null");
            OJIfStatement ifExist = new OJIfStatement("!this." + propertyWrapper.fieldname() + ".isEmpty()");
            ifExist.addToThenPart("throw new RuntimeException(\"Property is a one and already has a value!\")");
            ifNotNull.addToThenPart(ifExist);
			ifNotNull.addToThenPart("this." + propertyWrapper.fieldname() + ".add(" + propertyWrapper.fieldname() + ")");
			singleAdder.getBody().addToStatements(ifNotNull);
		} else {
			OJField failedConstraints = new OJField("violations", new OJPathName("java.util.List").addToGenerics(TinkerGenerationUtil.TumlConstraintViolation));
			failedConstraints.setInitExp(propertyWrapper.validator() + "(" + propertyWrapper.fieldname() + ")");
			singleAdder.getBody().addToLocals(failedConstraints);
			OJIfStatement ifValidated = new OJIfStatement(propertyWrapper.fieldname() + " != null && violations.isEmpty()");
			ifValidated.addToThenPart("this." + propertyWrapper.fieldname() + ".add(" + propertyWrapper.fieldname() + ")");
			singleAdder.getBody().addToStatements(ifValidated);
			ifValidated.addToElseIfCondition("!violations.isEmpty()", "throw new TumlConstraintViolationException(violations)");
			owner.addToImports(TinkerGenerationUtil.TumlConstraintViolationException);
		}
		owner.addToOperations(singleAdder);
	}

	public static void buildSetter(OJAnnotatedClass owner, PropertyWrapper pWrap) {
		OJAnnotatedOperation setter = new OJAnnotatedOperation(pWrap.setter());
		setter.addParam(pWrap.fieldname(), pWrap.javaBaseTypePath());
		if (pWrap.isReadOnly()) {
			setter.setVisibility(OJVisibilityKind.PROTECTED);
		}
		PropertyWrapper otherEnd = new PropertyWrapper(pWrap.getOtherEnd());
		if (pWrap.hasOtherEnd() && !pWrap.isEnumeration() && pWrap.isOneToOne()) {
			OJIfStatement ifNotNull = new OJIfStatement(pWrap.fieldname() + " != null");
			ifNotNull.addToThenPart(pWrap.fieldname() + "." + otherEnd.clearer() + "()");
			ifNotNull.addToThenPart(pWrap.fieldname() + ".initialiseProperty(" + TumlClassOperations.propertyEnumName(otherEnd.getOwningType()) + "."
					+ otherEnd.fieldname() + ", false)");
			owner.addToImports(TumlClassOperations.getPathName(otherEnd.getOwningType()).append(TumlClassOperations.propertyEnumName(otherEnd.getOwningType())));
			setter.getBody().addToStatements(ifNotNull);
		}
		setter.getBody().addToStatements(pWrap.clearer() + "()");
		setter.getBody().addToStatements(pWrap.adder() + "(" + pWrap.fieldname() + ")");
		owner.addToOperations(setter);
	}
}
