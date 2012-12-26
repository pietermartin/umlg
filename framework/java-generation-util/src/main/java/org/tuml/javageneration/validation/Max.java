package org.tuml.javageneration.validation;

import org.tuml.java.metamodel.OJPathName;


public class Max implements Validation {

	private int max;

	public int getMax() {
		return max;
	}

	public Max(int max) {
		super();
		this.max = max;
	}

	@Override
	public String toStringForMethod() {
		return String.valueOf(getMax());
	}

	@Override
	public String toNewRuntimeTumlValidation() {
		return "new Max(" + String.valueOf(max) + ")";
	}

	@Override
	public OJPathName getPathName() {
		return new OJPathName("org.tuml.runtime.validation.Max");
	}

	@Override
	public String toJson() {
		return "\\\"max\\\": " + String.valueOf(max);
	}
}
