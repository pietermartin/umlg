package org.umlg.javageneration.validation;

import org.umlg.java.metamodel.OJPathName;


public class RangeDouble implements Validation {
	private Number min;
	private Number max;

	public RangeDouble(Number min, Number max) {
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
		return getMin().toString() + "D, " + getMax().toString() + "D";
	}

	@Override
	public String toNewRuntimeTumlValidation() {
		return "new RangeDouble(" + min.toString() + "D, " + max.toString() + "D)";
	}
	
	@Override
	public OJPathName getPathName() {
		return new OJPathName("org.umlg.runtime.validation.RangeDouble");
	}
	
	@Override
	public String toJson() {
		return "\\\"range\\\": {\\\"min\\\": " + String.valueOf(min) + ", \\\"max\\\": " + String.valueOf(max) + "}";
	}

}
