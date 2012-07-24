package org.tuml.javageneration.ocl.visitor.tojava;

import java.util.List;

import org.eclipse.ocl.expressions.OperationCallExp;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Operation;

public class OclUnionExprToJava extends BaseHandleOperationExp {

	@Override
	public String handleOperationExp(OperationCallExp<Classifier, Operation> oc, String sourceResult, List<String> argumentResults) {
		if (argumentResults.size()!=1) {
			throw new IllegalStateException("Union operation must have one and only one argument!");
		}
		StringBuilder result = new StringBuilder();
		result.append(sourceResult);
		result.append(".union(");
		result.append(argumentResults.get(0));
		result.append(")");
		return result.toString();
	}

}
