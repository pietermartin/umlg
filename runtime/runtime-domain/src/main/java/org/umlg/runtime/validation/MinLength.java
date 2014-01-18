package org.umlg.runtime.validation;

public class MinLength implements UmlgValidation {
	private int minLength;

	public MinLength(int minLength) {
		super();
		this.minLength = minLength;
	}
}
