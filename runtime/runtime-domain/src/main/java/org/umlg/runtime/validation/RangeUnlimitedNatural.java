package org.umlg.runtime.validation;

public class RangeUnlimitedNatural implements UmlgValidation {
    private long min;
    private long max;

    public RangeUnlimitedNatural(long min, long max) {
        super();
        this.min = min;
        this.max = max;
    }
}
