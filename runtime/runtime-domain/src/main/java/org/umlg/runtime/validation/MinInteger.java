package org.umlg.runtime.validation;

public class MinInteger implements UmlgValidation {
	private int min;

	public MinInteger(int min) {
		super();
		this.min = min;
	}
}
