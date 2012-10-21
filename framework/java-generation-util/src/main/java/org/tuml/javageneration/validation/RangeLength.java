package org.tuml.javageneration.validation;

import org.opaeum.java.metamodel.OJPathName;


public class RangeLength implements Validation {

	private int minLength;
	private int maxLength;
	public int getMinLength() {
		return minLength;
	}
	public int getMaxLength() {
		return maxLength;
	}
	public RangeLength(int minLength, int maxLength) {
		super();
		this.minLength = minLength;
		this.maxLength = maxLength;
	}
	@Override
	public String toStringForMethod() {
		return String.valueOf(getMinLength()) + ", " + String.valueOf(getMaxLength());
	}
	
	@Override
	public String toNewRuntimeTumlValidation() {
		return "new RangeLength(" + String.valueOf(minLength) + ", " + String.valueOf(maxLength) + ")";
	}
	
	@Override
	public OJPathName getPathName() {
		return new OJPathName("org.tuml.runtime.validation.RangeLength");
	}
	
	@Override
	public String toJson() {
		return "\\\"rangeLength\\\": {\\\"min\\\": " + String.valueOf(minLength) + ", \\\"max\\\": " + String.valueOf(maxLength) + "}";
	}

}
