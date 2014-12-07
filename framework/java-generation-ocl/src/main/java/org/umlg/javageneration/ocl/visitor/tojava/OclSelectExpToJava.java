package org.umlg.javageneration.ocl.visitor.tojava;

import java.util.List;

import org.eclipse.ocl.expressions.IteratorExp;
import org.eclipse.ocl.expressions.Variable;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Parameter;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.java.metamodel.annotation.OJAnnotatedOperation;
import org.umlg.javageneration.ocl.util.UmlgOclUtil;
import org.umlg.javageneration.ocl.visitor.HandleIteratorExp;
import org.umlg.javageneration.util.UmlgClassOperations;

public class OclSelectExpToJava implements HandleIteratorExp {

	/**
	 * Generates something like below
	 * 
	 * return sourceCollection.select(
	 * 		new BooleanExpressionWithV<ElementType>() {
	 * 			@Override
	 * 			public Boolean evaluate(ElementType e) {
	 * 				return e.expr;
	 * 			});
	 * 		}
	 */
	public String handleIteratorExp(OJAnnotatedClass ojClass, IteratorExp<Classifier, Parameter> callExp, String sourceResult, List<String> variableResults, String bodyResult) {
		if (variableResults.size() != 1) {
			throw new IllegalStateException("An ocl select iterator expression may only have on iterator, i.e. variable");
		}
		Variable<Classifier, Parameter> variable = callExp.getIterator().get(0);
		String variableType = UmlgClassOperations.className(variable.getType());
		StringBuilder result = new StringBuilder(sourceResult);
		result.append(".select(");

		String oclVariable = UmlgOclUtil.extractVariable(variableResults.get(0));
		result.append(oclVariable);
		result.append("->");
		if (bodyResult.endsWith("()")) {
            String bodyResultWithoutParenthises = bodyResult.substring(0, bodyResult.length() - 2);
            OJAnnotatedOperation bodyOperation = ojClass.findOperation(bodyResultWithoutParenthises);
            bodyOperation.addParam(variable.getName(), UmlgClassOperations.getPathName(variable.getType()));
            result.append(bodyResultWithoutParenthises + "(" + variable.getName() + ")");
        } else {
		    result.append(bodyResult);
        }
		result.append(")");


//		result.append("new ");
//		result.append(HandleIteratorExp.BooleanExpressionEvaluator);
//		result.append("<");
//		result.append(variableType);
//		result.append(">() {\n");
//		result.append("    @Override\n");
//		result.append("    public Boolean evaluate(");
//		result.append(UmlgOclUtil.removeVariableInit(variableResults.get(0)));
//		result.append(") {\n");
//		result.append("        return ");
//        //if the body result is an operation then it needs the variable passed in.
//        if (bodyResult.endsWith("()")) {
//            String bodyResultWithoutParenthises = bodyResult.substring(0, bodyResult.length() - 2);
//            OJAnnotatedOperation bodyOperation = ojClass.findOperation(bodyResultWithoutParenthises);
//            bodyOperation.addParam(variable.getName(), UmlgClassOperations.getPathName(variable.getType()));
//            result.append(bodyResultWithoutParenthises + "(" + variable.getName() + ")");
//        } else {
//		    result.append(bodyResult);
//        }
//		result.append(";\n    }");
//		result.append("\n})");
		return result.toString();
	}
}
