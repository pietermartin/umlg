package org.tuml.javageneration.validation;

import org.opaeum.java.metamodel.OJPathName;


public class MaxLength implements Validation {

	private int maxLength;


	public MaxLength(int maxLength) {
		super();
		this.maxLength = maxLength;
	}
	
	@Override
	public String toStringForMethod() {
		return String.valueOf(getMaxLength());
	}

	public int getMaxLength() {
		return maxLength;
	}

	@Override
	public String toNewRuntimeTumlValidation() {
		return "new MaxLength("+String.valueOf(maxLength)+")";
	}
	
	@Override
	public OJPathName getPathName() {
		return new OJPathName("org.tuml.runtime.validation.MaxLength");
	}
	
	@Override
	public String toJson() {
		return "\\\"maxLength\\\": " + String.valueOf(maxLength);
	}


}
