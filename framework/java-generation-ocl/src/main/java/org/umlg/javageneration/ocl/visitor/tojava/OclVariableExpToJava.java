package org.umlg.javageneration.ocl.visitor.tojava;

import org.eclipse.ocl.expressions.Variable;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Parameter;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.javageneration.ocl.visitor.HandleVariableExp;
import org.umlg.javageneration.util.UmlgClassOperations;

public class OclVariableExpToJava implements HandleVariableExp {

	protected OJAnnotatedClass ojClass;

	public String handleVariable(Variable<Classifier, Parameter> vd, String initResult) {
		String varName = vd.getName();
		Classifier type = vd.getType();
		
		return String.format("%s %s = %s;", UmlgClassOperations.className(type), varName, initResult);
	}

	@Override
	public HandleVariableExp setOJClass(OJAnnotatedClass ojClass) {
		this.ojClass = ojClass;
		return this;
	}
}