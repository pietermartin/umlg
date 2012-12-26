package org.tuml.javageneration.ocl.visitor.tojava;

import java.util.List;

import org.eclipse.ocl.expressions.OperationCallExp;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Operation;
import org.tuml.java.metamodel.OJParameter;
import org.tuml.java.metamodel.OJPathName;
import org.tuml.java.metamodel.OJTryStatement;
import org.tuml.java.metamodel.OJVisibilityKind;
import org.tuml.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.javageneration.util.TinkerGenerationUtil;

public class OclOclIsUndefinedExpToJava extends BaseHandleOperationExp {

	/**
	 * Wraps the source expression in a try catch block. If
	 * OclIsInvalidException is caught return true else false
	 */
	@Override
	public String handleOperationExp(OperationCallExp<Classifier, Operation> oc, String sourceResult, List<String> argumentResults) {
		if (!argumentResults.isEmpty()) {
			throw new IllegalStateException("oclIsUndefined operation can not have arguments!");
		}
		String operationName = oc.getReferredOperation().getName();
		OJAnnotatedOperation oper = new OJAnnotatedOperation(operationName + this.ojClass.countOperationsStartingWith(operationName), new OJPathName("Boolean"));
		this.ojClass.addToOperations(oper);
		oper.setVisibility(OJVisibilityKind.PRIVATE);
		OJTryStatement ojTryStatement = new OJTryStatement();
		ojTryStatement.getTryPart().addToStatements("return " + sourceResult + " == null");
		ojTryStatement.setCatchParam(new OJParameter("e", TinkerGenerationUtil.tumlOclIsInvalidException.getCopy()));
		this.ojClass.addToImports(TinkerGenerationUtil.tumlOclIsInvalidException.getCopy());
		ojTryStatement.getCatchPart().addToStatements("return true");
		oper.getBody().addToStatements(ojTryStatement);
		return oper.getName() + "()";
	}
}
