package org.tuml.restlet.client.json.validation;

import java.util.Map;

public class MinLength implements TumlValidation {
	private int minLength;

	public MinLength(Integer value) {
		super();
		this.minLength = value;
	}
}
