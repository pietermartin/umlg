package org.tuml.javageneration.visitor.operation;

import java.io.File;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.opaeum.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.framework.Visitor;
import org.tuml.javageneration.Workspace;
import org.tuml.javageneration.naming.Namer;
import org.tuml.javageneration.util.OclUtil;
import org.tuml.javageneration.util.OperationWrapper;
import org.tuml.javageneration.visitor.BaseVisitor;

public class OperationImplementorSimple extends BaseVisitor implements Visitor<org.eclipse.uml2.uml.Operation> {

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
		} else if (operOwner instanceof org.eclipse.uml2.uml.Class) {
			ojClass = findOJClass((org.eclipse.uml2.uml.Class) operOwner);
			OJAnnotatedOperation ojOper = addOperSignature(ojClass, oper);
			if (oper.isQuery()) {
				addQueryBody(ojClass, ojOper, oper);
			}
		} else {
			throw new IllegalStateException("Operations are only supperted on Interfaces and Classes, not on " + operOwner.toString());
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
		File oclFile = this.workspace.writeOclFile(ocl, Namer.qualifiedName(operWrapper));
		String java = OclUtil.oclToJava(oclFile);
		ojOper.getBody().addToStatements(java);
	}
}
