package org.umlg.javageneration.validation;

import org.umlg.java.metamodel.OJPathName;


public class MaxReal implements Validation {

	private Double max;

	public Number getMax() {
		return max;
	}

	public MaxReal(Double max) {
		super();
		this.max = max;
	}

	@Override
	public String toStringForMethod() {
		return getMax().toString();
	}

	@Override
	public String toNewRuntimeTumlValidation() {
		return "new MaxReal(" + max.toString() + ")";
	}

	@Override
	public OJPathName getPathName() {
		return new OJPathName("org.umlg.runtime.validation.MaxReal");
	}

	@Override
	public String toJson() {
		return "\\\"max\\\": " + String.valueOf(max);
	}
}
