package org.umlg.restlet.client.json.validation;

public class MaxLength implements UmlgValidation {
	private int maxLength;

	public MaxLength(Integer value) {
		super();
		this.maxLength = value;
	}
}
