package org.umlg.runtime.validation;

public class RangeLong implements UmlgValidation {
    private long min;
	private long max;

	public RangeLong(long min, long max) {
		super();
		this.min = min;
		this.max = max;
	}
}
