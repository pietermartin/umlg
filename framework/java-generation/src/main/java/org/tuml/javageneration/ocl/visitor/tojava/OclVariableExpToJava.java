package org.tuml.javageneration.ocl.visitor.tojava;

import org.eclipse.ocl.expressions.Variable;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Parameter;
import org.tuml.javageneration.ocl.visitor.HandleVariableExp;
import org.tuml.javageneration.util.TumlClassOperations;

public class OclVariableExpToJava implements HandleVariableExp {

	public String handleVariable(Variable<Classifier, Parameter> vd, String initResult) {
		String varName = vd.getName();
		Classifier type = vd.getType();
		return String.format("%s %s = %s;", TumlClassOperations.className(type), varName, initResult);
	}
}