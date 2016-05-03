package org.umlg.javageneration.util;

import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.internal.operations.OperationOperations;

import java.util.ArrayList;
import java.util.List;

public class UmlgOperationOperations extends OperationOperations {

	public static String asOclSignature(Operation oper) {
		StringBuilder sb = new StringBuilder();
		if (oper.getClass_() != null) {
			sb.append(oper.getClass_().getName());
		} else if (oper.getDatatype() != null) {
			sb.append(oper.getDatatype().getName());
		} else {
			throw new IllegalStateException("Operation only supported on Class and Datatype");
		}
		sb.append("::");
		sb.append(oper.getName());
		sb.append("(");
		List<Parameter> parametersExceptReturn = getParametersExceptReturn(oper);
		int count = 1;
		for (Parameter param : parametersExceptReturn) {
			sb.append(param.getName());
			sb.append(": ");
			sb.append(param.getType().getQualifiedName());
			if (count++ != parametersExceptReturn.size()) {
				sb.append(", ");
			}
		}
		sb.append(")");
		Parameter returnResult = oper.getReturnResult();
		if (returnResult!=null) {
			sb.append(" : ");
			if (UmlgMultiplicityOperations.isMany(returnResult)) {
				sb.append(UmlgGenerationUtil.getCollectionInterface(returnResult));
				sb.append("(");
				sb.append(returnResult.getType().getQualifiedName());
				sb.append(")");
			} else {
				sb.append(returnResult.getType().getQualifiedName());
			}
		}
		return sb.toString();
	}

	public static List<Parameter> getParametersExceptReturn(Operation oper) {
		List<Parameter> result = new ArrayList<Parameter>();
		for (Parameter param : oper.getOwnedParameters()) {
			if (param.getDirection() != ParameterDirectionKind.RETURN_LITERAL) {
				result.add(param);
			}
		}
		return result;
	}
}
