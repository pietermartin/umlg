package org.tuml.javageneration.ocl.visitor.java;

import java.util.List;

import org.eclipse.ocl.expressions.IteratorExp;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.expressions.Variable;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Parameter;
import org.tuml.javageneration.ocl.visitor.HandleIteratorExp;
import org.tuml.javageneration.util.TumlClassOperations;

public class OclCollectNestedToJava implements HandleIteratorExp {

	/**
	 * Generates something like below
	 * 
	 * return getCollection().select(
	 * 		new BodyExpressionEvaluator<ResultType, ElementType>() {
	 * 			@Override
	 * 			public ResultType evaluate(ElementType e) {
	 * 				return e.expr;
	 * 			});
	 * 		}
	 */
	public String handleIteratorExp(IteratorExp<Classifier, Parameter> callExp, String sourceResult, List<String> variableResults, String bodyResult) {
		if (variableResults.size() != 1) {
			throw new IllegalStateException("An ocl select iterator expression may only have on iterator, i.e. variable");
		}
		
		Variable<Classifier, Parameter> variable = callExp.getIterator().get(0);
		String variableType = TumlClassOperations.className(variable.getType());
		
		OCLExpression<Classifier> body = callExp.getBody();
		String bodyType = TumlClassOperations.className(body.getType());
		
		StringBuilder result = new StringBuilder(sourceResult);
		result.append(".collectNested(");
		result.append("new BodyExpressionEvaluator<");
		result.append(bodyType);
		result.append(", ");
		result.append(variableType);
		result.append(">() {\n");
		result.append("    @Override\n");
		result.append("    public ");
		result.append(bodyType);
		result.append(" evaluate(");
		result.append(variableType);
		result.append(" e) {\n");
		result.append("        return e.");
		result.append(bodyResult);
		result.append(";\n    }");
		result.append("\n})");
		return result.toString();
	}
	
}
