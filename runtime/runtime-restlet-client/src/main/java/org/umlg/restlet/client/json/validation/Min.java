package org.umlg.restlet.client.json.validation;

public class Min implements TumlValidation {
	private int min;

	public Min(Integer value) {
		super();
		this.min = value;
	}
}
