package org.tuml.restlet.client.json.validation;

import org.joda.time.DateTime;

import java.util.Map;

/**
 * Date: 2013/02/02
 * Time: 7:30 PM
 */
public enum ValidationEnum {

    DATETIME_VALIDATION("dateTime"),
    DATE_VALIDATION("date"),
    EMAIL("email"),
    MAX("max"),
    MAX_LENGTH("maxLength"),
    MIN("min"),
    MIN_LENGTH("minLength"),
    RANGE("range"),
    RANGE_LENGTH("rangeLength"),
    TIME_VALIDATION("time"),
    TUM_CONSTRAINT_VALIDATION("constraint"),
    URL("url");

    private String name;

    private ValidationEnum(String name) {
        this.name = name;
    }

    public static ValidationEnum fromName(String name) {
        if (name.equals("dateTime")) {
              return DATETIME_VALIDATION;
        } else if (name.equals("date")) {
            return DATE_VALIDATION;
        } else if (name.equals("email")) {
            return EMAIL;
        } else if (name.equals("max")) {
            return MAX;
        } else if (name.equals("maxLength")) {
            return MAX_LENGTH;
        } else if (name.equals("min")) {
            return MIN;
        } else if (name.equals("minLength")) {
            return MIN_LENGTH;
        } else if (name.equals("range")) {
            return RANGE;
        } else if (name.equals("rangeLength")) {
            return RANGE_LENGTH;
        } else if (name.equals("time")) {
            return TIME_VALIDATION;
        } else if (name.equals("constraint")) {
            return TUM_CONSTRAINT_VALIDATION;
        } else if (name.equals("url")) {
            return URL;
        } else {
            throw new IllegalStateException(String.format("Unkown validation name \"%s\"", new Object[]{name}));
        }
    }

    public TumlValidation getValidation(Object value) {
        switch (this) {
            case DATETIME_VALIDATION:
                //TODO
                return new DateTimeValidation();
            case DATE_VALIDATION:
                //TODO
                return new DateValidation();
            case EMAIL:
                //TODO
                return new Email();
            case MAX:
                return new Max((Map<String, Integer>) value);
            case MAX_LENGTH:
                return new MaxLength((Map<String, Integer>) value);
            case MIN:
                return new Min((Map<String, Integer>) value);
            case MIN_LENGTH:
                return new MinLength((Map<String, Integer>) value);
            case RANGE:
                return new Range((Map<String, Integer>)value);
            case RANGE_LENGTH:
                return new RangeLength((Map<String, Integer>)value);
            case TIME_VALIDATION:
                //TODO
                return new TimeValidation();
            case TUM_CONSTRAINT_VALIDATION:
                throw new RuntimeException("Not yet implemented");
            case URL:
                throw new RuntimeException("Not yet implemented");
            default:
                throw new IllegalStateException(String.format("Unkown validation enum %s", new Object[]{name()}));
        }

    }

}
