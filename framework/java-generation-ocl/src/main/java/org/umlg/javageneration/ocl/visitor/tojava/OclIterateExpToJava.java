package org.umlg.javageneration.ocl.visitor.tojava;

import java.util.List;

import org.eclipse.ocl.expressions.IterateExp;
import org.eclipse.ocl.expressions.Variable;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Parameter;
import org.umlg.javageneration.ocl.visitor.HandleIterateExp;
import org.umlg.javageneration.util.TumlClassOperations;

public class OclIterateExpToJava implements HandleIterateExp {

	/**
	 * Generates something like below
	 * 
	 */
	public String handleIterateExp(IterateExp<Classifier, Parameter> callExp, String sourceResult, List<String> variableResults, String resultResult, String bodyResult) {
		if (variableResults.size() != 1) {
			throw new IllegalStateException("An ocl select iterator expression may only have on iterator, i.e. variable");
		}
		
		String resultVariableName = callExp.getResult().getName();
		String resultType = TumlClassOperations.className(callExp.getResult().getType());
		Variable<Classifier, Parameter> variable = callExp.getIterator().get(0);
		String variableType = TumlClassOperations.className(variable.getType());
		String iterVariableName = variable.getName();
		StringBuilder result = new StringBuilder(sourceResult);
		result.append(".iterate(");
		result.append("new ");
		result.append(HandleIterateExp.IterateExpressionAccumulator);
		result.append("<");
		result.append(resultType);
		result.append(", ");
		result.append(variableType);
		result.append(">() {\n");
		result.append("    @Override\n");
		result.append("    public ");
		result.append(resultType);
		result.append(" accumulate(");
		result.append(resultType);
		result.append(" ");
		result.append(resultVariableName);
		result.append(", ");
		result.append(variableType);
		result.append(" ");
		result.append(iterVariableName);
		result.append(") {\n");
		result.append("        return ");
		result.append(bodyResult);
		result.append(";\n    }\n\n");
		
		result.append("    @Override\n");
		result.append("    public ");
		result.append(resultType);
		result.append(" initAccumulator()");
		result.append(" {\n");
		result.append("        ");
		result.append(resultResult);
		result.append("\n        return ");
		result.append(resultVariableName);
		result.append(";\n    }");
		
		result.append("\n})");
		return result.toString();
	}
}
