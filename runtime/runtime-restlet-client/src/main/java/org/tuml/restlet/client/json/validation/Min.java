package org.tuml.restlet.client.json.validation;

import java.util.Map;

public class Min implements TumlValidation {
	private int min;

	public Min(Integer value) {
		super();
		this.min = value;
	}
}
