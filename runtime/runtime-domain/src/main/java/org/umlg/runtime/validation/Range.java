package org.umlg.runtime.validation;

public class Range implements UmlgValidation {
private int min;
	private int max;

	public Range(int min, int max) {
		super();
		this.min = min;
		this.max = max;
	}
}
