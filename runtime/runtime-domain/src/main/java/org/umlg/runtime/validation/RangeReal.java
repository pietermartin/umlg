package org.umlg.runtime.validation;

public class RangeReal implements UmlgValidation {
private double min;
	private double max;

	public RangeReal(double min, double max) {
		super();
		this.min = min;
		this.max = max;
	}
}
