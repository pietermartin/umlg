package org.umlg.runtime.validation;

public class MinLength implements TumlValidation {
	private int minLength;

	public MinLength(int minLength) {
		super();
		this.minLength = minLength;
	}
}
