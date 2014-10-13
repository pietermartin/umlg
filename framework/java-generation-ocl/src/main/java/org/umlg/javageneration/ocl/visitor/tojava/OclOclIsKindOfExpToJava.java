package org.umlg.javageneration.ocl.visitor.tojava;

import org.eclipse.ocl.expressions.OperationCallExp;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Operation;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.OJVisibilityKind;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;

import java.util.List;

public class OclOclIsKindOfExpToJava extends BaseHandleOperationExp {

	/**
	 * Wraps the source expression in a try catch block. If
	 * OclIsInvalidException is caught return true else false
	 */
	@Override
	public String handleOperationExp(OperationCallExp<Classifier, Operation> oc, String sourceResult, List<String> argumentResults) {
		if (argumentResults.size() != 1) {
			throw new IllegalStateException("oclIsTypeOd operation must have one argument!");
		}
		String operationName = oc.getReferredOperation().getName();
		OJAnnotatedOperation oper = new OJAnnotatedOperation(operationName + this.ojClass.countOperationsStartingWith(operationName), new OJPathName("Boolean"));
		this.ojClass.addToOperations(oper);
		oper.setVisibility(OJVisibilityKind.PRIVATE);
        if (sourceResult.equals("self")) {
            sourceResult = "this";
        }
        oper.getBody().addToStatements("return " + argumentResults.get(0) + ".class.isAssignableFrom(" + sourceResult + ".getClass())");
		return oper.getName() + "()";
	}
}
