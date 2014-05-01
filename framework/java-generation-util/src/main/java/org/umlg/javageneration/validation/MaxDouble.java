package org.umlg.javageneration.validation;

import org.umlg.java.metamodel.OJPathName;


public class MaxDouble implements Validation {

	private Number max;

	public Number getMax() {
		return max;
	}

	public MaxDouble(Number max) {
		super();
		this.max = max;
	}

	@Override
	public String toStringForMethod() {
		return getMax().toString() + "D";
	}

	@Override
	public String toNewRuntimeTumlValidation() {
		return "new MaxDouble(" + max.toString() + "D)";
	}

	@Override
	public OJPathName getPathName() {
		return new OJPathName("org.umlg.runtime.validation.MaxDouble");
	}

	@Override
	public String toJson() {
		return "\\\"max\\\": " + String.valueOf(max);
	}
}
