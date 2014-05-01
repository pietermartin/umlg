package org.umlg.javageneration.validation;

import org.umlg.java.metamodel.OJPathName;


public class MaxLong implements Validation {

	private Number max;

	public Number getMax() {
		return max;
	}

	public MaxLong(Number max) {
		super();
		this.max = max;
	}

	@Override
	public String toStringForMethod() {
		return getMax().toString();
	}

	@Override
	public String toNewRuntimeTumlValidation() {
		return "new MaxLong(" + max.toString() + ")";
	}

	@Override
	public OJPathName getPathName() {
		return new OJPathName("org.umlg.runtime.validation.MaxLong");
	}

	@Override
	public String toJson() {
		return "\\\"max\\\": " + String.valueOf(max);
	}
}
