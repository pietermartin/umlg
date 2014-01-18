package org.umlg.restlet.client.json.validation;

public class MinLength implements UmlgValidation {
	private int minLength;

	public MinLength(Integer value) {
		super();
		this.minLength = value;
	}
}
