package org.tuml.restlet.client.json.validation;

import java.util.Map;

public class Max implements TumlValidation {
    private int max;

    public Max(Map<String, Integer> value) {
        super();
        this.max = value.get("max");
    }
}
