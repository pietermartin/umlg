package org.umlg.restlet.client.json.validation;

public class Min implements UmlgValidation {
	private int min;

	public Min(Integer value) {
		super();
		this.min = value;
	}
}
