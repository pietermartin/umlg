package org.tuml.javageneration.ocl.visitor;

import java.util.List;

import org.eclipse.ocl.expressions.TupleLiteralExp;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Property;
import org.tuml.java.metamodel.annotation.OJAnnotatedClass;

public interface HandleTupleLiteralExp {
	String handleTupleLiteralExp(TupleLiteralExp<Classifier, Property> literalExp, List<String> partResults);
	HandleTupleLiteralExp setOJClass(OJAnnotatedClass ojClass);
}
