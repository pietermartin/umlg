package org.tuml.javageneration.visitor.property;

import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.OJIfStatement;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedField;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.visitor.BaseVisitor;

public class OnePropertyVisitor extends BaseVisitor implements Visitor<Property> {

	@Override
	public void visitBefore(Property p) {
		PropertyWrapper propertyWrapper = new PropertyWrapper(p);
		//TODO qualifiers
		if (propertyWrapper.isOne() && !propertyWrapper.isQualifier()) {
			OJAnnotatedClass owner = findOJClass(p);
			buildOneGetter(owner, propertyWrapper);
			if (propertyWrapper.isOneToOne()) {
				buildOneToOneSetter(owner, propertyWrapper);
			} else {
				//Many to One
				buildManyToOneSetter(owner, propertyWrapper);
			}
		}
	}

	@Override
	public void visitAfter(Property element) {

	}
	
	/*
	 * ToOne properties are stored in a List similar to toMany. The first element is returned
	 */
	private void buildOneGetter(OJAnnotatedClass owner, PropertyWrapper propertyWrapper) {
		OJAnnotatedOperation getter = new OJAnnotatedOperation(propertyWrapper.getter(), propertyWrapper.javaBaseTypePath());
		OJAnnotatedField tmpField = new OJAnnotatedField("tmp", propertyWrapper.javaTypePath());
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
	
	private void buildOneToOneSetter(OJAnnotatedClass owner, PropertyWrapper propertyWrapper) {
		OJAnnotatedOperation setter = buildSetterOutline(owner, propertyWrapper);
		//Get old value
		OJAnnotatedField oldValue = new OJAnnotatedField("oldValue", propertyWrapper.javaTypePath());
		oldValue.setInitExp("this." + propertyWrapper.getter() + "()");
		setter.getBody().addToLocals(oldValue);
	}

	private void buildManyToOneSetter(OJAnnotatedClass owner, PropertyWrapper propertyWrapper) {
		OJAnnotatedOperation setter = buildSetterOutline(owner, propertyWrapper);
	}

	private OJAnnotatedOperation buildSetterOutline(OJAnnotatedClass owner, PropertyWrapper propertyWrapper) {
		OJAnnotatedOperation oneToOneSetter = new OJAnnotatedOperation(propertyWrapper.setter());
		oneToOneSetter.addParam(propertyWrapper.fieldname(), propertyWrapper.javaTypePath());
		owner.addToOperations(oneToOneSetter);
		return oneToOneSetter;
	}
	
}
