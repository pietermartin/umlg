package org.tuml.javageneration.visitor.property;

import java.util.logging.Logger;

import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Property;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.ocl.TumlOcl2Java;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.visitor.BaseVisitor;
import org.tuml.javageneration.visitor.clazz.ClassBuilder;
import org.tuml.ocl.TumlOcl2Parser;

public class PropertyVisitor extends BaseVisitor implements Visitor<Property> {

	private static Logger logger = Logger.getLogger(DerivedPropertyVisitor.class.getPackage().getName());
	
	public PropertyVisitor(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Property p) {
		PropertyWrapper propertyWrapper = new PropertyWrapper(p);
		OJAnnotatedClass owner = findOJClass(p);
		if (!propertyWrapper.isDerived() && !propertyWrapper.isQualifier() && !propertyWrapper.isForQualifier()) {
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
		String java;
		OJAnnotatedOperation initVariables = owner.findOperation(ClassBuilder.INIT_VARIABLES);
		if (propertyWrapper.hasOclDefaultValue()) {
			String ocl = propertyWrapper.getOclDerivedValue();
			initVariables.setComment(String.format("Implements the ocl statement for initialization variable '%s'\n<pre>\n%s\n</pre>", propertyWrapper.getName(), ocl));
			logger.info(String.format("About to parse ocl expression \n%s", new Object[] { ocl }));
			OCLExpression<Classifier> constraint = TumlOcl2Parser.INSTANCE.parseOcl(ocl);
			java = TumlOcl2Java.oclToJava(owner, constraint);
			if (propertyWrapper.isMany()) {
				//This is used in the initial value
				owner.addToImports("java.util.Arrays");
			}
//			java = "//TODO " + constraint.toString();
			initVariables.getBody().addToStatements(propertyWrapper.setter() + "(" + java + ")");
		} else {
			java = propertyWrapper.getInitValue();
			initVariables.getBody().addToStatements(propertyWrapper.setter() + "(" + java + ")");
		}
	}

}
