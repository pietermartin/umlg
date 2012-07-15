package org.tuml.javageneration.ocl.visitor;

import java.util.List;

import org.eclipse.ocl.expressions.IteratorExp;
import org.eclipse.ocl.utilities.PredefinedType;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Parameter;
import org.tuml.javageneration.ocl.visitor.tojava.OclAnyExpToJava;
import org.tuml.javageneration.ocl.visitor.tojava.OclCollectExpToJava;
import org.tuml.javageneration.ocl.visitor.tojava.OclCollectNestedExpToJava;
import org.tuml.javageneration.ocl.visitor.tojava.OclSelectExpToJava;

public enum OclIteratorExpEnum implements HandleIteratorExp {

	SELECT(new OclSelectExpToJava()), COLLECT(new OclCollectExpToJava()), COLLECT_NESTED(new OclCollectNestedExpToJava()), ANY(new OclAnyExpToJava());
	private HandleIteratorExp implementor;
	
	private OclIteratorExpEnum(HandleIteratorExp implementor) {
		this.implementor = implementor;
	}
	
	public static OclIteratorExpEnum from(String name) {
		if (name.equals(PredefinedType.SELECT_NAME)) {
			return SELECT;
		} else if (name.equals(PredefinedType.COLLECT_NAME)) {
			return COLLECT;
		} else if (name.equals(PredefinedType.COLLECT_NESTED_NAME)) {
			return COLLECT_NESTED;
		} else if (name.equals(PredefinedType.ANY_NAME)) {
			return ANY;
		} else {
			throw new RuntimeException(String.format("Not yet implemented, %s", name));
		}
	}
	
	public String handleIteratorExp(IteratorExp<Classifier, Parameter> callExp, String sourceResult, List<String> variableResults, String bodyResult) {
		return implementor.handleIteratorExp(callExp, sourceResult, variableResults, bodyResult);
	}
}
