package org.tuml.javageneration.validation;

import org.opaeum.java.metamodel.OJPathName;


public class MinLength implements Validation {

	private int minLength;

	public int getMinLength() {
		return minLength;
	}

	public MinLength(int minLength) {
		super();
		this.minLength = minLength;
	}

	@Override
	public String toStringForMethod() {
		return String.valueOf(getMinLength());
	}
	
	@Override
	public String toNewRuntimeTumlValidation() {
		return "new MinLength(" + String.valueOf(minLength) + ")";
	}
	
	@Override
	public OJPathName getPathName() {
		return new OJPathName("org.tuml.runtime.validation.MinLength");
	}
	
	@Override
	public String toJson() {
		return "\\\"minLength\\\": " + String.valueOf(minLength);
	}


	
}
