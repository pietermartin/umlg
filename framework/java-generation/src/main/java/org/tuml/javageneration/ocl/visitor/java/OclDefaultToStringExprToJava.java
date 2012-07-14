package org.tuml.javageneration.ocl.visitor.java;

import java.util.List;

import org.eclipse.ocl.expressions.OperationCallExp;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Operation;
import org.tuml.javageneration.ocl.visitor.HandleOperationExp;

public class OclDefaultToStringExprToJava implements HandleOperationExp {

	@Override
	public String handleOperationExp(OperationCallExp<Classifier, Operation> oc, String sourceResult, List<String> argumentResults) {
		return oc.toString();
	}

}
