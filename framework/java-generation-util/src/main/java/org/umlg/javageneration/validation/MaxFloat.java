package org.umlg.javageneration.validation;

import org.umlg.java.metamodel.OJPathName;


public class MaxFloat implements Validation {

	private Number max;

	public Number getMax() {
		return max;
	}

	public MaxFloat(Number max) {
		super();
		this.max = max;
	}

	@Override
	public String toStringForMethod() {
		return getMax().toString() + "F";
	}

	@Override
	public String toNewRuntimeTumlValidation() {
		return "new MaxFloat(" + max.toString() + "F)";
	}

	@Override
	public OJPathName getPathName() {
		return new OJPathName("org.umlg.runtime.validation.MaxFloat");
	}

	@Override
	public String toJson() {
		return "\\\"max\\\": " + String.valueOf(max);
	}
}
