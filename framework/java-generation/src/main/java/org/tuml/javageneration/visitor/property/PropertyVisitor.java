package org.tuml.javageneration.visitor.property;

import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.visitor.BaseVisitor;
import org.tuml.javageneration.visitor.clazz.ClassBuilder;

public class PropertyVisitor extends BaseVisitor implements Visitor<Property> {

	@Override
	public void visitBefore(Property p) {
		PropertyWrapper propertyWrapper = new PropertyWrapper(p);
		OJAnnotatedClass owner = findOJClass(p);
		if (!propertyWrapper.isDerived() && !propertyWrapper.isQualifier()) {
			buildField(owner, propertyWrapper);
			buildRemover(owner, propertyWrapper);
			buildClearer(owner, propertyWrapper);
		}
		if (!propertyWrapper.isDerived() && propertyWrapper.getDefaultValue()!=null) {
			addInitialization(owner, propertyWrapper);
		}
	}

	@Override
	public void visitAfter(Property element) {
	}

	private void addInitialization(OJAnnotatedClass owner, PropertyWrapper propertyWrapper) {
		OJAnnotatedOperation initVariables = owner.findOperation(ClassBuilder.INIT_VARIABLES);
		initVariables.getBody().addToStatements(propertyWrapper.setter() + "(" + propertyWrapper.getOclDefaultValueAsJava() + ")");
	}

}
