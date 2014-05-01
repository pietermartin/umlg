package org.umlg.runtime.validation;

public class RangeDouble implements UmlgValidation {
    private double min;
	private double max;

	public RangeDouble(double min, double max) {
		super();
		this.min = min;
		this.max = max;
	}
}
