package org.tuml.javageneration.ocl.visitor.tojava;

import org.opaeum.java.metamodel.annotation.OJAnnotatedClass;
import org.tuml.javageneration.ocl.visitor.HandleOperationExp;

public abstract class BaseHandleOperationExp implements HandleOperationExp {

	protected OJAnnotatedClass ojClass;

	@Override
	public HandleOperationExp setOJClass(OJAnnotatedClass ojClass) {
		this.ojClass = ojClass;
		return this;
	}

}
