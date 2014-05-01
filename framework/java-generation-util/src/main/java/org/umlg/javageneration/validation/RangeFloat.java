package org.umlg.javageneration.validation;

import org.umlg.java.metamodel.OJPathName;


public class RangeFloat implements Validation {
	private Number min;
	private Number max;

	public RangeFloat(Number min, Number max) {
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
		return getMin().toString() + "F, " + getMax().toString() + "F";
	}

	@Override
	public String toNewRuntimeTumlValidation() {
		return "new RangeFloat(" + min.toString() + "F, " + max.toString() + "F)";
	}
	
	@Override
	public OJPathName getPathName() {
		return new OJPathName("org.umlg.runtime.validation.RangeFloat");
	}
	
	@Override
	public String toJson() {
		return "\\\"range\\\": {\\\"min\\\": " + String.valueOf(min) + ", \\\"max\\\": " + String.valueOf(max) + "}";
	}

}
