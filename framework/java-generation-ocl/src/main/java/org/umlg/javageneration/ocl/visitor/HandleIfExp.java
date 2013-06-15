package org.umlg.javageneration.ocl.visitor;

import org.eclipse.ocl.expressions.IfExp;
import org.eclipse.uml2.uml.Classifier;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;

public interface HandleIfExp {
	String handleIfExp(IfExp<Classifier> ifExp, String conditionResult, String thenResult, String elseResult);
	HandleIfExp setOJClass(OJAnnotatedClass ojClass);
}
