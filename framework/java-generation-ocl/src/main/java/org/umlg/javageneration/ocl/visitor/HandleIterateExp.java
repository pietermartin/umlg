package org.umlg.javageneration.ocl.visitor;

import java.util.List;

import org.eclipse.ocl.expressions.IterateExp;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Parameter;

public interface HandleIterateExp {
	final static String IterateExpressionAccumulator = "IterateExpressionAccumulator";
	String handleIterateExp(IterateExp<Classifier, Parameter> callExp, String sourceResult, List<String> variableResults, String resultResult, String bodyResult);
}
