package org.tuml.javageneration.ocl.visitor;

import java.io.File;

import org.eclipse.uml2.uml.Property;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.Workspace;
import org.tuml.javageneration.naming.Namer;
import org.tuml.javageneration.util.OclUtil;
import org.tuml.javageneration.util.PropertyWrapper;
import org.tuml.javageneration.visitor.BaseVisitor;

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
			File oclFile = this.workspace.writeOclFile(ocl, Namer.qualifiedName(p));
			String java = OclUtil.oclToJava(oclFile);
			getter.getBody().addToStatements(java);
			owner.addToOperations(getter);
		}
	}

	@Override
	public void visitAfter(Property p) {

	}

}
