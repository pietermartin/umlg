package org.umlg.javageneration.ocl.visitor.tojava;

import java.util.List;

import org.eclipse.ocl.expressions.OperationCallExp;
import org.eclipse.ocl.uml.impl.TypeExpImpl;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Operation;
import org.umlg.javageneration.util.UmlgClassOperations;

public class OclAllInstancesExprToJava extends BaseHandleOperationExp {

	@Override
	public String handleOperationExp(OperationCallExp<Classifier, Operation> oc, String sourceResult, List<String> argumentResults) {
		if (!argumentResults.isEmpty()) {
			throw new IllegalStateException("AllInstances operation must have no arguments!");
		}
        this.ojClass.addToImports(UmlgClassOperations.getPathName(((TypeExpImpl)oc.getSource()).getReferredType()));
		StringBuilder result = new StringBuilder();
		result.append(sourceResult);
		result.append(".allInstances()");
		return result.toString();
	}

}
