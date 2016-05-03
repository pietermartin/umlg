package org.umlg.javageneration.visitor.operation;

import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.uml2.uml.*;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.ocl.UmlgOcl2Java;
import org.umlg.javageneration.util.OperationWrapper;
import org.umlg.javageneration.visitor.BaseVisitor;
import org.umlg.ocl.UmlgOcl2Parser;

import java.util.logging.Logger;

public class OperationImplementorSimple extends BaseVisitor implements Visitor<org.eclipse.uml2.uml.Operation> {

	private static Logger logger = Logger.getLogger(OperationImplementorSimple.class.getPackage().getName());
	
	public OperationImplementorSimple(Workspace workspace) {
		super(workspace);
	}

	@Override
	public void visitBefore(Operation oper) {
		Element operOwner = oper.getOwner();
		if (oper.getBodyCondition() != null && !oper.isQuery()) {
			throw new IllegalStateException(String.format("Operation %s on %s has a bodyCondition but is not a query.",
					new Object[] { oper.getName(), ((NamedElement) operOwner).getName() }));
		}
		OJAnnotatedClass ojClass;
		if (operOwner instanceof Interface) {
			ojClass = findOJClass((Interface) operOwner);
			addOperSignature(ojClass, oper);
		} else if ((operOwner instanceof org.eclipse.uml2.uml.Class) || (operOwner instanceof Enumeration)) {
			ojClass = findOJClass((org.eclipse.uml2.uml.Classifier) operOwner);
			OJAnnotatedOperation ojOper = addOperSignature(ojClass, oper);
			if (oper.isQuery()) {
				addQueryBody(ojClass, ojOper, oper);
			}
		} else {
			throw new IllegalStateException("Operations are only supported on Interfaces and Classes, not on " + operOwner.toString());
		}
	}

	@Override
	public void visitAfter(Operation oper) {
	}

	private OJAnnotatedOperation addOperSignature(OJAnnotatedClass ojClass, Operation oper) {
		OperationWrapper operWrapper = new OperationWrapper(oper);
		OJAnnotatedOperation ojOper = new OJAnnotatedOperation(oper.getName(), operWrapper.getReturnParamPathName());
		ojOper.addToParameters(operWrapper.getOJParametersExceptReturn());
		ojClass.addToOperations(ojOper);
		return ojOper;
	}

	private void addQueryBody(OJAnnotatedClass ojClass, OJAnnotatedOperation ojOper, Operation oper) {
		OperationWrapper operWrapper = new OperationWrapper(oper);
		String ocl = operWrapper.getOclBodyCondition();
		ojOper.setComment(String.format("Implements the ocl statement for operation body condition '%s'\n<pre>\n%s\n</pre>", operWrapper.getName(), ocl));
		logger.fine(String.format("About to parse ocl expression \n%s", new Object[] { ocl }));
		OCLExpression<Classifier> oclExp = UmlgOcl2Parser.INSTANCE.parseOcl(ocl);
		ojOper.getBody().addToStatements("return " + UmlgOcl2Java.oclToJava(oper, ojClass, oclExp));
	}
}
