package org.umlg.javageneration.ocl.visitor.tojava;

import org.apache.commons.lang.StringUtils;
import org.eclipse.ocl.expressions.OperationCallExp;
import org.eclipse.uml2.uml.*;
import org.umlg.java.metamodel.OJParameter;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.OJTryStatement;
import org.umlg.java.metamodel.OJVisibilityKind;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.util.DataTypeEnum;
import org.umlg.javageneration.util.UmlgClassOperations;
import org.umlg.javageneration.util.UmlgGenerationUtil;
import org.umlg.javageneration.util.UmlgPropertyOperations;

import java.util.List;

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
		Type sourceType = oc.getSource().getType();
        OJAnnotatedOperation oper = new OJAnnotatedOperation(operationName + this.ojClass.countOperationsStartingWith(operationName), new OJPathName("Boolean"));
        if (!(sourceType instanceof PrimitiveType) && !(sourceType instanceof Enumeration) && sourceType instanceof DataType) {
			oper.addParam(StringUtils.uncapitalize("param"), DataTypeEnum.getPathNameFromDataType((DataType) sourceType));
		} else if (sourceType instanceof PrimitiveType) {
			oper.addParam(StringUtils.uncapitalize("param"), UmlgPropertyOperations.umlPrimitiveTypeToJava(sourceType));
        } else {
            oper.addParam(StringUtils.uncapitalize("param"), UmlgClassOperations.getPathName(sourceType));
        }
		this.ojClass.addToOperations(oper);
		oper.setVisibility(OJVisibilityKind.PRIVATE);
		OJTryStatement ojTryStatement = new OJTryStatement();
		ojTryStatement.getTryPart().addToStatements("return " + StringUtils.uncapitalize("param") + " == null");
		ojTryStatement.setCatchParam(new OJParameter("e", UmlgGenerationUtil.umlgOclIsInvalidException.getCopy()));
		this.ojClass.addToImports(UmlgGenerationUtil.umlgOclIsInvalidException.getCopy());
		ojTryStatement.getCatchPart().addToStatements("return true");
		oper.getBody().addToStatements(ojTryStatement);
		return oper.getName() + "(" + sourceResult + ")";
	}
}
