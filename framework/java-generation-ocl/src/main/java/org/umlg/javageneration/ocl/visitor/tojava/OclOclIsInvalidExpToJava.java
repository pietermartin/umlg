package org.umlg.javageneration.ocl.visitor.tojava;

import java.util.List;

import org.eclipse.ocl.expressions.OperationCallExp;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Operation;
import org.umlg.java.metamodel.OJParameter;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.OJTryStatement;
import org.umlg.java.metamodel.OJVisibilityKind;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.util.TinkerGenerationUtil;

public class OclOclIsInvalidExpToJava extends BaseHandleOperationExp {

	/**
	 * Wraps the source expression in a try catch block. If
	 * OclIsInvalidException is caught return true else false
	 */
	@Override
	public String handleOperationExp(OperationCallExp<Classifier, Operation> oc, String sourceResult, List<String> argumentResults) {
		if (!argumentResults.isEmpty()) {
			throw new IllegalStateException("oclIsInvalid operation can not have arguments!");
		}
		String operationName = oc.getReferredOperation().getName();
		OJAnnotatedOperation oper = new OJAnnotatedOperation(operationName + this.ojClass.countOperationsStartingWith(operationName), new OJPathName("Boolean"));
		this.ojClass.addToOperations(oper);
		oper.setVisibility(OJVisibilityKind.PRIVATE);
		OJTryStatement ojTryStatement = new OJTryStatement();
		ojTryStatement.getTryPart().addToStatements(sourceResult);
		ojTryStatement.getTryPart().addToStatements("return false");
		ojTryStatement.setCatchParam(new OJParameter("e", TinkerGenerationUtil.tumlOclIsInvalidException.getCopy()));
		this.ojClass.addToImports(TinkerGenerationUtil.tumlOclIsInvalidException.getCopy());
		ojTryStatement.getCatchPart().addToStatements("return true");
		oper.getBody().addToStatements(ojTryStatement);
		return oper.getName() + "()";
	}
}
