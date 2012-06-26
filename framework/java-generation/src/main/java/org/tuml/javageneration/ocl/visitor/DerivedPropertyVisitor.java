package org.tuml.javageneration.ocl.visitor;

import org.eclipse.uml2.uml.Property;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.visitor.BaseVisitor;
import org.tuml.javageneration.visitor.property.PropertyWrapper;

public class DerivedPropertyVisitor extends BaseVisitor implements Visitor<Property> {

	@Override
	public void visitBefore(Property p) {
		PropertyWrapper propertyWrapper = new PropertyWrapper(p);
		if (propertyWrapper.isDerived()) {
			String ocl = propertyWrapper.getOcl();
		}
	}

	@Override
	public void visitAfter(Property p) {
		
	}
	
}
