package org.tuml.javageneration.ocl.visitor;

import org.eclipse.ocl.expressions.Variable;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Parameter;
import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;

public interface HandleVariableExp {
	String handleVariable(Variable<Classifier, Parameter> vd, String initResult);
	HandleVariableExp setOJClass(OJAnnotatedClass ojClass);
}
