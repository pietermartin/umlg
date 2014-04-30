package org.umlg.runtime.validation;

import org.umlg.runtime.util.EmailValidationUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

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
		return EmailValidationUtils.isValid(email);
	}
}
