package org.umlg.runtime.validation;

import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.quartz.CronExpression;
import org.umlg.runtime.types.Password;

public class UmlgValidator {

    public static boolean validateMinLength(String s, int min) {
        if (s == null) {
            return true;
        }
        int length = s.length();
        return length >= min;
    }

    public static boolean validateMaxLength(String s, int max) {
        if (s == null) {
            return true;
        }
        int length = s.length();
        return length <= max;
    }

    public static boolean validateRangeLength(String s, int min, int max) {
        if (s == null) {
            return true;
        }
        int length = s.length();
        return length >= min && length <= max;
    }

    public static boolean validateMinInteger(Integer value, int minValue) {
        if (value == null) {
            return true;
        }
        return value.compareTo(minValue) != -1;
    }

    public static boolean validateMaxInteger(Integer value, int max) {
        if (value == null) {
            return true;
        }
        return value.compareTo(max) != 1;
    }

    public static boolean validateRangeInteger(Integer value, int min, int max) {
        if (value == null) {
            return true;
        }
        return value <= max && value >= min;
    }

    public static boolean validateMinUnlimitedNatural(Integer value, int minValue) {
        if (value == null) {
            return true;
        }
        return value.compareTo(minValue) != -1;
    }

    public static boolean validateMaxUnlimitedNatural(Integer value, int max) {
        if (value == null) {
            return true;
        }
        return value.compareTo(max) != 1;
    }

    public static boolean validateRangeUnlimitedNatural(Integer value, int min, int max) {
        if (value == null) {
            return true;
        }
        return value <= max && value >= min;
    }

    public static boolean validateValidUnlimitedNatural(Integer value) {
        if (value == null) {
            return true;
        }
        return value >= 0;
    }

    public static boolean validateMinLong(Long value, long minValue) {
        if (value == null) {
            return true;
        }
        return value.compareTo(minValue) != -1;
    }

    public static boolean validateMaxLong(Long value, long max) {
        if (value == null) {
            return true;
        }
        return value.compareTo(max) != 1;
    }

    public static boolean validateRangeLong(Long value, long min, long max) {
        if (value == null) {
            return true;
        }
        return value <= max && value >= min;
    }

    public static boolean validateMinFloat(Float value, float minValue) {
        if (value == null) {
            return true;
        }
        return value.compareTo(minValue) != -1;
    }

    public static boolean validateMaxFloat(Float value, float max) {
        if (value == null) {
            return true;
        }
        return value.compareTo(max) != 1;
    }

    public static boolean validateRangeFloat(Float value, float min, float max) {
        if (value == null) {
            return true;
        }
        return value <= max && value >= min;
    }

    public static boolean validateMinDouble(Double value, double minValue) {
        if (value == null) {
            return true;
        }
        return value.compareTo(minValue) != -1;
    }

    public static boolean validateMaxDouble(Double value, double max) {
        if (value == null) {
            return true;
        }
        return value.compareTo(max) != 1;
    }

    public static boolean validateRangeDouble(Double value, double min, double max) {
        if (value == null) {
            return true;
        }
        return value <= max && value >= min;
    }

    public static boolean validateMinReal(Double value, double minValue) {
        if (value == null) {
            return true;
        }
        return value.compareTo(minValue) != -1;
    }

    public static boolean validateMaxReal(Double value, double max) {
        if (value == null) {
            return true;
        }
        return value.compareTo(max) != 1;
    }

    public static boolean validateRangeReal(Double value, double min, double max) {
        if (value == null) {
            return true;
        }
        return value <= max && value >= min;
    }

    public static boolean validateUrl(String url, String protocol, String host, int port, String regexp, String flags) {
        throw new RuntimeException("Not yet implemented!");
    }

    public static boolean validateEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    public static boolean validateHost(String host) {
        return InetAddressValidator.getInstance().isValid(host) || DomainValidator.getInstance().isValid(host);
    }

    public static boolean validateQuartzCron(String cron) {
        return CronExpression.isValidExpression(cron);
    }

    public static boolean validatePassword(Password password) {
        return true;
    }

}
