package org.umlg.runtime.validation;

public class MinLong implements UmlgValidation {
	private long min;

	public MinLong(long min) {
		super();
		this.min = min;
	}
}
