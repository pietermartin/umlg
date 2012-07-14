package org.tuml.javageneration.visitor.property;

import java.util.logging.Logger;

import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.Workspace;
import org.tuml.javageneration.ocl.TumlOcl2Java;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.util.TinkerGenerationUtil;
import org.tuml.javageneration.visitor.BaseVisitor;
import org.tuml.ocl.TumlOcl2Parser;

public class DerivedPropertyVisitor extends BaseVisitor implements Visitor<Property> {

	private static Logger logger = Logger.getLogger(DerivedPropertyVisitor.class.getPackage().getName());

	public DerivedPropertyVisitor(Workspace workspace) {
		super(workspace);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.tuml.framework.Visitor#visitBefore(org.eclipse.uml2.uml.Element)
	 */
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
			getter.setComment(String.format("Implements the ocl statement for derived property '%s'\n<pre>\n %s </pre>\n", propertyWrapper.getName(), ocl));
			logger.fine(String.format("About to parse ocl expression \n%s", new Object[] { ocl }));
			OCLExpression<Classifier> oclExp = TumlOcl2Parser.INSTANCE.parseOcl(ocl);
			getter.getBody().addToStatements("return " + TumlOcl2Java.oclToJava(oclExp));
			owner.addToImports(TinkerGenerationUtil.tumlOclStdCollectionLib);
			owner.addToImports("java.util.*");
			owner.addToOperations(getter);
		}
	}

	@Override
	public void visitAfter(Property p) {

	}

}
