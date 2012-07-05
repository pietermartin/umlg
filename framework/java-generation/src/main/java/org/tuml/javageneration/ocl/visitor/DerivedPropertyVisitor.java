package org.tuml.javageneration.ocl.visitor;

import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.visitor.BaseVisitor;

public class DerivedPropertyVisitor extends BaseVisitor implements Visitor<Property> {

	@Override
	public void visitBefore(Property p) {
		PropertyWrapper propertyWrapper = new PropertyWrapper(p);
		if (propertyWrapper.isDerived()) {
			OJAnnotatedClass owner = findOJClass(p);
			OJAnnotatedOperation getter;
			if (propertyWrapper.isOne()) {
				getter = new OJAnnotatedOperation(propertyWrapper.getter(), propertyWrapper.javaBaseTypePath());
			} else {
				getter = new OJAnnotatedOperation(propertyWrapper.getter(), propertyWrapper.javaTypePath());
			}
			getter.getBody().addToStatements(propertyWrapper.getOclDefaultValueAsJava());
			owner.addToOperations(getter);
		}
	}

	@Override
	public void visitAfter(Property p) {

	}

}
