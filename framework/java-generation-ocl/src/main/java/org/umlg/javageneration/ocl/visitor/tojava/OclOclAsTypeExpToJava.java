package org.umlg.javageneration.ocl.visitor.tojava;

import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.expressions.OperationCallExp;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Type;
import org.umlg.java.metamodel.OJVisibilityKind;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.util.UmlgClassOperations;

import java.util.List;

public class OclOclAsTypeExpToJava extends BaseHandleOperationExp {

	/**
	 * Wraps the source expression in a try catch block. If
	 * OclIsInvalidException is caught return true else false
	 */
	@Override
	public String handleOperationExp(OperationCallExp<Classifier, Operation> oc, String sourceResult, List<String> argumentResults) {
		if (argumentResults.size() != 1) {
			throw new IllegalStateException("oclAsType operation must have one argument!");
		}

        List<OCLExpression<Classifier>> arguments = oc.getArgument();
        Type argumentType = arguments.get(0).getType();

		String operationName = oc.getReferredOperation().getName();
		OJAnnotatedOperation oper = new OJAnnotatedOperation(operationName + this.ojClass.countOperationsStartingWith(operationName), UmlgClassOperations.getPathName(argumentType));
        oper.addParam(sourceResult, UmlgClassOperations.getPathName(argumentType));
		this.ojClass.addToOperations(oper);
		oper.setVisibility(OJVisibilityKind.PRIVATE);
        if (sourceResult.equals("self")) {
            sourceResult = "this";
        }
        oper.getBody().addToStatements("return ((" + argumentResults.get(0) + ")" + sourceResult + ")");
        if (sourceResult.equals("self")) {
            sourceResult = "this";
        }
        return oper.getName() + "(" + sourceResult + ")";
	}
}
