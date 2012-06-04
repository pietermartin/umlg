package org.tuml.javageneration.visitor.property;

import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.visitor.BaseVisitor;

public class PropertyVisitor extends BaseVisitor implements Visitor<Property> {

	@Override
	public void visitBefore(Property p) {
		PropertyWrapper propertyWrapper = new PropertyWrapper(p);
		if (!propertyWrapper.isDerived() && !propertyWrapper.isQualifier()) {
			OJAnnotatedClass owner = findOJClass(p);
			buildField(owner, propertyWrapper);
			buildRemover(owner, propertyWrapper);
			buildClearer(owner, propertyWrapper);
		}
	}

	@Override
	public void visitAfter(Property element) {
	}

}
