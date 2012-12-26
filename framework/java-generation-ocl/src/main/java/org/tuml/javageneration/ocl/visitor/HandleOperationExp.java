package org.tuml.javageneration.ocl.visitor;

import java.util.List;

import org.eclipse.ocl.expressions.OperationCallExp;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Operation;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;

public interface HandleOperationExp {
	String handleOperationExp(OperationCallExp<Classifier, Operation> oc, String sourceResult, List<String> argumentResults);
	HandleOperationExp setOJClass(OJAnnotatedClass ojClass);
}
