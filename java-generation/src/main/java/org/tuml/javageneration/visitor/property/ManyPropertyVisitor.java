package org.tuml.javageneration.visitor.property;

import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.visitor.BaseVisitor;

public class ManyPropertyVisitor extends BaseVisitor implements Visitor<Property> {

	@Override
	public void visitBefore(Property p) {
		PropertyWrapper propertyWrapper = new PropertyWrapper(p);
		if (propertyWrapper.isMany() && !propertyWrapper.isQualifier()) {
			OJAnnotatedClass owner = findOJClass(p);
			buildGetter(owner, propertyWrapper);
		}
	}

	@Override
	public void visitAfter(Property element) {

	}
	
	private void buildGetter(OJAnnotatedClass owner, PropertyWrapper propertyWrapper) {
		OJAnnotatedOperation getter = new OJAnnotatedOperation(propertyWrapper.getter(), propertyWrapper.javaTypePath());
		getter.getBody().addToStatements("return this." + propertyWrapper.fieldname());
		owner.addToOperations(getter);
	}
	
}
