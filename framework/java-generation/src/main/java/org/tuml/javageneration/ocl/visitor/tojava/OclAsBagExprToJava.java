package org.tuml.javageneration.ocl.visitor.tojava;

import java.util.List;

import org.eclipse.ocl.expressions.OperationCallExp;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Operation;
import org.tuml.javageneration.ocl.visitor.HandleOperationExp;

public class OclAsBagExprToJava implements HandleOperationExp {

	@Override
	public String handleOperationExp(OperationCallExp<Classifier, Operation> oc, String sourceResult, List<String> argumentResults) {
		StringBuilder result = new StringBuilder();
		result.append(sourceResult);
		result.append(".asBag()");
		return result.toString();
	}

}
