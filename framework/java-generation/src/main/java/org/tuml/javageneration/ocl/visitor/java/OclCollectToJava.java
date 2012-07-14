package org.tuml.javageneration.ocl.visitor.java;

import java.util.List;

import org.eclipse.ocl.expressions.IteratorExp;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.expressions.Variable;
import org.eclipse.ocl.uml.impl.CollectionTypeImpl;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Parameter;
import org.tuml.javageneration.ocl.visitor.HandleIteratorExp;
import org.tuml.javageneration.util.TumlClassOperations;

public class OclCollectToJava implements HandleIteratorExp {

	/**
	 * Generates something like below
	 * 
	 * return getSet().select(
	 * 		new BooleanBodyExpressionEvaluator<OclTestCollection>() {
	 * 			@Override
	 * 			public Boolean evaluate(OclTestCollection e) {
	 * 				return getName().equals("john");
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
		
		String flattenedType;
		Classifier c = body.getType();
		if (c instanceof CollectionTypeImpl) {
			CollectionTypeImpl collectionTypeImpl = (CollectionTypeImpl)c;
			flattenedType = TumlClassOperations.className(collectionTypeImpl.getElementType());
		} else {
			flattenedType = TumlClassOperations.className(c);
		}
		
		StringBuilder result = new StringBuilder(sourceResult);
		result.append(".<");
		result.append(flattenedType);
		result.append(", ");
		result.append(bodyType);
		result.append(">collect(");
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
