package org.umlg.javageneration.ocl.visitor.tojava;

import org.eclipse.ocl.expressions.OperationCallExp;
import org.eclipse.ocl.uml.PrimitiveType;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Operation;

import java.util.List;

public class OclNotExprToJava extends BaseHandleOperationExp {

	@Override
	public String handleOperationExp(OperationCallExp<Classifier, Operation> oc, String sourceResult, List<String> argumentResults) {
        StringBuilder result = new StringBuilder();
        result.append(sourceResult);
        result.append(" == false");
        return result.toString();
	}

}
