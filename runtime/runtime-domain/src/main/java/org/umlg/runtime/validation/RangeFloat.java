package org.umlg.runtime.validation;

public class RangeFloat implements UmlgValidation {
    private float min;
	private float max;

	public RangeFloat(float min, float max) {
		super();
		this.min = min;
		this.max = max;
	}
}
