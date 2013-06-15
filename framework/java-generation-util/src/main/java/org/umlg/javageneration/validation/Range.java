package org.umlg.javageneration.validation;

import org.umlg.java.metamodel.OJPathName;


public class Range implements Validation {
	private int min;
	private int max;

	public Range(int min, int max) {
		super();
		this.min = min;
		this.max = max;
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	@Override
	public String toStringForMethod() {
		return String.valueOf(getMin()) + ", " + String.valueOf(getMax());
	}

	@Override
	public String toNewRuntimeTumlValidation() {
		return "new Range(" + String.valueOf(min) + ", " + String.valueOf(max) + ")";
	}
	
	@Override
	public OJPathName getPathName() {
		return new OJPathName("org.umlg.runtime.validation.Range");
	}
	
	@Override
	public String toJson() {
		return "\\\"range\\\": {\\\"min\\\": " + String.valueOf(min) + ", \\\"max\\\": " + String.valueOf(max) + "}";
	}

}
