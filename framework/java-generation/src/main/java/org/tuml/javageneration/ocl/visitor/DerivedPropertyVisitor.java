package org.tuml.javageneration.ocl.visitor;

import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.Workspace;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.visitor.BaseVisitor;
import org.tuml.ocl.TumlOcl2Parser;
import org.tuml.ocl.java.TumlOcl2Java;

public class DerivedPropertyVisitor extends BaseVisitor implements Visitor<Property> {

	public DerivedPropertyVisitor(Workspace workspace) {
		super(workspace);
	}

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
			String ocl = propertyWrapper.getOclDerivedValue();
			OCLExpression<Classifier> oclExp = TumlOcl2Parser.INSTANCE.parseOcl(ocl);
			getter.getBody().addToStatements(TumlOcl2Java.oclToJava(oclExp));
			owner.addToOperations(getter);
		}
	}

	@Override
	public void visitAfter(Property p) {

	}

}
