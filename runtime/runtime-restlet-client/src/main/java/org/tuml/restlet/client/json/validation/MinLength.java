package org.tuml.restlet.client.json.validation;

import java.util.Map;

public class MinLength implements TumlValidation {
	private int minLength;

	public MinLength(Map<String, Integer> value) {
		super();
		this.minLength = value.get("minLength");
	}
}
