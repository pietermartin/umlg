package org.umlg.runtime.adaptor;

import java.util.Arrays;
import java.util.function.BiPredicate;

/**
 * Date: 2015/06/27
 * Time: 2:15 PM
 */
public enum StringArrayContains implements BiPredicate<String[], Object> {

    within {
        @Override
        public boolean test(final String[] first, final Object second) {
            return Arrays.asList(first).contains(second);
        }
    }, without {
        @Override
        public boolean test(final String[] first, final Object second) {
            return !Arrays.asList(first).contains(second);
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract boolean test(final String[] first, final Object second);

    /**
     * Produce the opposite representation of the current {@code Contains} enum.
     */
    @Override
    public StringArrayContains negate() {
        return this.equals(within) ? without : within;
    }
}
