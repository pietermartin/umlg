package org.tuml.restlet.client.json.validation;

import java.util.Map;

public class MaxLength implements TumlValidation {
	private int maxLength;

	public MaxLength(Integer value) {
		super();
		this.maxLength = value;
	}
}
