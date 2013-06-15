package org.umlg.javageneration.validation;

import org.umlg.java.metamodel.OJPathName;


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
		return new OJPathName("org.umlg.runtime.validation.MinLength");
	}
	
	@Override
	public String toJson() {
		return "\\\"minLength\\\": " + String.valueOf(minLength);
	}


	
}
