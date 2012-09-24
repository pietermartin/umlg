package org.tuml.runtime.validation;

public class MaxLength implements TumlValidation {
	private int maxLength;

	public MaxLength(int maxLength) {
		super();
		this.maxLength = maxLength;
	}
}
