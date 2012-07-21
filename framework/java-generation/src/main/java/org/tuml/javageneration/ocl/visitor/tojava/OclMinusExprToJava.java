package org.tuml.javageneration.ocl.visitor.tojava;

import java.util.List;

import org.eclipse.ocl.expressions.OperationCallExp;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Operation;

public class OclMinusExprToJava extends BaseHandleOperationExp {

	@Override
	public String handleOperationExp(OperationCallExp<Classifier, Operation> oc, String sourceResult, List<String> argumentResults) {
		if (!argumentResults.isEmpty() && argumentResults.size() != 1) {
			throw new IllegalStateException("The minus (-) operation must have zero or one argument!");
		}
		StringBuilder result = new StringBuilder();
		result.append("-");
		result.append(sourceResult);
		if (!argumentResults.isEmpty()) {
			result.append(argumentResults.get(0));
		}
		return result.toString();
	}

}
