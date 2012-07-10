package org.tuml.javageneration.visitor.property;

import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.Workspace;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.visitor.BaseVisitor;
import org.tuml.javageneration.visitor.clazz.ClassBuilder;
import org.tuml.ocl.TumlOcl2Parser;

public class PropertyVisitor extends BaseVisitor implements Visitor<Property> {

	public PropertyVisitor(Workspace workspace) {
		super(workspace);
	}

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
		String java;
		if (propertyWrapper.hasOclDefaultValue()) {
			OCLExpression<Classifier> constraint = TumlOcl2Parser.INSTANCE.parseOcl(propertyWrapper.getOclDefaultValue());
			java = "//TODO " + constraint.toString();
		} else {
			java = propertyWrapper.getInitValue();
		}
		initVariables.getBody().addToStatements(propertyWrapper.setter() + "(" + java + ")");
	}

}
