package org.tuml.restlet.client.json.validation;

import java.util.Map;

public class MaxLength implements TumlValidation {
	private int maxLength;

	public MaxLength(Map<String, Integer> value) {
		super();
		this.maxLength = value.get("maxLength");
	}
}
