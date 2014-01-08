package org.umlg.javageneration.visitor.property;

import java.util.logging.Logger;

import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Property;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.ocl.TumlOcl2Java;
import org.umlg.javageneration.util.PropertyWrapper;
import org.umlg.javageneration.visitor.BaseVisitor;
import org.umlg.ocl.UmlgOcl2Parser;

public class DerivedPropertyVisitor extends BaseVisitor implements Visitor<Property> {

	private static Logger logger = Logger.getLogger(DerivedPropertyVisitor.class.getPackage().getName());

	public DerivedPropertyVisitor(Workspace workspace) {
		super(workspace);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umlg.framework.Visitor#visitBefore(org.eclipse.uml2.uml.Element)
	 */
	@Override
	public void visitBefore(Property p) {
		PropertyWrapper propertyWrapper = new PropertyWrapper(p);
		if (propertyWrapper.isDerived() && !propertyWrapper.isDerivedUnion()) {
			OJAnnotatedClass owner = findOJClass(p);
			OJAnnotatedOperation getter;
			if (propertyWrapper.isOne()) {
				getter = new OJAnnotatedOperation(propertyWrapper.getter(), propertyWrapper.javaBaseTypePath());
			} else {
				getter = new OJAnnotatedOperation(propertyWrapper.getter(), propertyWrapper.javaTypePath());
			}
			String ocl = propertyWrapper.getOclDerivedValue();
			getter.setComment(String.format("Implements the ocl statement for derived property '%s'\n<pre>\n%s\n</pre>", propertyWrapper.getName(), ocl));
			logger.info(String.format("About to parse ocl expression \n%s", new Object[] { ocl }));
			OCLExpression<Classifier> oclExp = UmlgOcl2Parser.INSTANCE.parseOcl(ocl);
			getter.getBody().addToStatements("return " + TumlOcl2Java.oclToJava(owner, oclExp));
			owner.addToOperations(getter);
		}
	}

	@Override
	public void visitAfter(Property p) {

	}

}
