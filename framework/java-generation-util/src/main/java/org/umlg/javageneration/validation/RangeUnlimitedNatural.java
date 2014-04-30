package org.umlg.javageneration.validation;

import org.umlg.java.metamodel.OJPathName;


public class RangeUnlimitedNatural implements Validation {
	private Integer min;
	private Integer max;

	public RangeUnlimitedNatural(Integer min, Integer max) {
		super();
		this.min = min;
		this.max = max;
	}

	public Number getMin() {
		return min;
	}

	public Number getMax() {
		return max;
	}

	@Override
	public String toStringForMethod() {
		return getMin().toString() + ", " + getMax().toString();
	}

	@Override
	public String toNewRuntimeTumlValidation() {
		return "new RangeUnlimitedNatural(" + min.toString() + ", " + max.toString() + ")";
	}
	
	@Override
	public OJPathName getPathName() {
		return new OJPathName("org.umlg.runtime.validation.RangeUnlimitedNatural");
	}
	
	@Override
	public String toJson() {
		return "\\\"range\\\": {\\\"min\\\": " + String.valueOf(min) + ", \\\"max\\\": " + String.valueOf(max) + "}";
	}

}
