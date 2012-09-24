package org.tuml.javageneration.validation;

import org.opaeum.java.metamodel.OJPathName;


public class Min implements Validation {

	private int min;

	public int getMin() {
		return min;
	}

	public Min(int min) {
		super();
		this.min = min;
	}

	@Override
	public String toStringForMethod() {
		return String.valueOf(getMin());
	}

	@Override
	public String toNewRuntimeTumlValidation() {
		return "new Min(" + String.valueOf(min) + ")";
	}
	
	@Override
	public OJPathName getPathName() {
		return new OJPathName("org.tuml.runtime.validation.Min");
	}
	
	@Override
	public String toJson() {
		return "\\\"min\\\": " + String.valueOf(min);
	}


}
