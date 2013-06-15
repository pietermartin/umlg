package org.umlg.javageneration.ocl.visitor.tojava;

import org.umlg.java.metamodel.annotation.OJAnnotatedClass;
import org.umlg.javageneration.ocl.visitor.HandleOperationExp;

public abstract class BaseHandleOperationExp implements HandleOperationExp {

	protected OJAnnotatedClass ojClass;

	@Override
	public HandleOperationExp setOJClass(OJAnnotatedClass ojClass) {
		this.ojClass = ojClass;
		return this;
	}

}
