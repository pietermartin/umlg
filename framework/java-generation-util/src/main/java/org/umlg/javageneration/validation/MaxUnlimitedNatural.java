package org.umlg.javageneration.validation;

import org.umlg.java.metamodel.OJPathName;


public class MaxUnlimitedNatural implements Validation {

	private Integer max;

	public Number getMax() {
		return max;
	}

	public MaxUnlimitedNatural(Integer max) {
		super();
		this.max = max;
	}

	@Override
	public String toStringForMethod() {
		return getMax().toString();
	}

	@Override
	public String toNewRuntimeTumlValidation() {
		return "new MaxUnlimitedNatural(" + max.toString() + ")";
	}

	@Override
	public OJPathName getPathName() {
		return new OJPathName("org.umlg.runtime.validation.MaxUnlimitedNatural");
	}

	@Override
	public String toJson() {
		return "\\\"max\\\": " + String.valueOf(max);
	}
}
