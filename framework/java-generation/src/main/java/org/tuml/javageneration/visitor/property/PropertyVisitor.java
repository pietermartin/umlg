package org.tuml.javageneration.visitor.property;

import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.OJIfStatement;
import org.opaeum.java.metamodel.OJVisibilityKind;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedField;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.visitor.BaseVisitor;

public class PropertyVisitor extends BaseVisitor implements Visitor<Property> {

	@Override
	public void visitBefore(Property p) {
		PropertyWrapper propertyWrapper = new PropertyWrapper(p);
		if (!propertyWrapper.isDerived() && !propertyWrapper.isQualifier()) {
			OJAnnotatedClass owner = findOJClass(p);
			buildField(owner, propertyWrapper);
			buildInternalAdder(owner, propertyWrapper);
			buildInternalRemover(owner, propertyWrapper);
			buildAdder(owner, propertyWrapper);
		}
	}

	@Override
	public void visitAfter(Property element) {
	}

	private void buildField(OJAnnotatedClass owner, PropertyWrapper propertyWrapper) {
		OJAnnotatedField field = new OJAnnotatedField(propertyWrapper.fieldname(), propertyWrapper.javaTumlTypePath());
		field.setStatic(propertyWrapper.isStatic());
		owner.addToFields(field);
	}

	private void buildInternalRemover(OJAnnotatedClass owner, PropertyWrapper propertyWrapper) {
		OJAnnotatedOperation internalRemover = new OJAnnotatedOperation(propertyWrapper.internalRemover());
		internalRemover.addParam(propertyWrapper.fieldname(), propertyWrapper.javaBaseTypePath());
		internalRemover.setVisibility(propertyWrapper.isReadOnly() ? OJVisibilityKind.PRIVATE : OJVisibilityKind.PUBLIC);
		internalRemover.getBody().addToStatements("this." + propertyWrapper.fieldname() + ".remove(" + propertyWrapper.fieldname() + ")");
		owner.addToOperations(internalRemover);
	}

	private void buildInternalAdder(OJAnnotatedClass owner, PropertyWrapper propertyWrapper) {
		OJAnnotatedOperation internalAdder = new OJAnnotatedOperation(propertyWrapper.internalAdder());
		internalAdder.addParam(propertyWrapper.fieldname(), propertyWrapper.javaBaseTypePath());
		internalAdder.setVisibility(propertyWrapper.isReadOnly() ? OJVisibilityKind.PRIVATE : OJVisibilityKind.PUBLIC);
		internalAdder.getBody().addToStatements("this." + propertyWrapper.fieldname() + ".add(" + propertyWrapper.fieldname() + ")");
		// TODO qualifiers
		// if (!map.getProperty().getQualifiers().isEmpty()) {
		// s.setExpression(s.getExpression().replace("val)", "val, " +
		// TinkerGenerationUtil.contructNameForQualifiedGetter(map) +
		// "(val))"));
		// }
		owner.addToOperations(internalAdder);
	}

	private void buildAdder(OJAnnotatedClass owner, PropertyWrapper propertyWrapper) {
		OJAnnotatedOperation adder = new OJAnnotatedOperation(propertyWrapper.adder());
		adder.addParam(propertyWrapper.fieldname(), propertyWrapper.javaBaseTypePath());
		OJIfStatement ifNotNull = new OJIfStatement(propertyWrapper.fieldname() + " != null");
		if (propertyWrapper.hasOtherEnd()) {
			PropertyWrapper otherEndPropertyWrapper = new PropertyWrapper(propertyWrapper.getOtherEnd());
			ifNotNull.addToThenPart(propertyWrapper.fieldname() + "." + otherEndPropertyWrapper.internalRemover() + "(" + propertyWrapper.fieldname() + "."
					+ otherEndPropertyWrapper.getter() + "())");
			ifNotNull.addToThenPart(propertyWrapper.fieldname() + "." + otherEndPropertyWrapper.internalAdder() + "(this)");
		}
		ifNotNull.addToThenPart(propertyWrapper.internalAdder() + "(" + propertyWrapper.fieldname() + ")");
		adder.getBody().addToStatements(ifNotNull);
		owner.addToOperations(adder);
	}

}
