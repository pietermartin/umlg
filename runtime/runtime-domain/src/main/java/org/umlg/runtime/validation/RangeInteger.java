package org.umlg.runtime.validation;

public class RangeInteger implements UmlgValidation {
    private int min;
    private int max;

    public RangeInteger(int min, int max) {
        super();
        this.min = min;
        this.max = max;
    }
}
