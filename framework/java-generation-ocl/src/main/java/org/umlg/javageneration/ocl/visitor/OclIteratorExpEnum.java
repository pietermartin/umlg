package org.umlg.javageneration.ocl.visitor;

import java.util.List;

import org.eclipse.ocl.expressions.IteratorExp;
import org.eclipse.ocl.utilities.PredefinedType;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Parameter;
import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.javageneration.ocl.visitor.tojava.*;

public enum OclIteratorExpEnum implements HandleIteratorExp {

	SELECT(new OclSelectExpToJava()),
    COLLECT(new OclCollectExpToJava()),
    COLLECT_NESTED(new OclCollectNestedExpToJava()),
    ANY(new OclAnyExpToJava()),
    IS_UNIQUE(new OclIsUniqueExpToJava()),
    EXISTS(new OclExistsExpToJava()),
    FOR_ALL(new OclForAllExpToJava()),
	SORTED_BY(new OclSortedByExpToJava());
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
        } else if (name.equals(PredefinedType.IS_UNIQUE_NAME)) {
            return IS_UNIQUE;
        } else if (name.equals(PredefinedType.EXISTS_NAME)) {
            return EXISTS;
        } else if (name.equals(PredefinedType.FOR_ALL_NAME)) {
            return FOR_ALL;
		} else if (name.equals(PredefinedType.SORTED_BY_NAME)) {
			return SORTED_BY;
		} else {
			throw new RuntimeException(String.format("Not yet implemented, %s", name));
		}
	}
	
	public String handleIteratorExp(OJAnnotatedClass ojClass, IteratorExp<Classifier, Parameter> callExp, String sourceResult, List<String> variableResults, String bodyResult) {
		return implementor.handleIteratorExp(ojClass, callExp, sourceResult, variableResults, bodyResult);
	}
}
