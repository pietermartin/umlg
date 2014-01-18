package org.umlg.runtime.validation;

public class RangeLength implements UmlgValidation {
private int min;
	private int max;

	public RangeLength(int min, int max) {
		super();
		this.min = min;
		this.max = max;
	}
}
