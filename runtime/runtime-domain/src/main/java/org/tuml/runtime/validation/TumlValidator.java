package org.tuml.runtime.validation;

import org.tuml.runtime.util.EmailValidationUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TumlValidator {

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

	public static boolean validateMin(Number value, long minValue) {
		if (value == null) {
			return true;
		} else if (value instanceof BigDecimal) {
			return ((BigDecimal) value).compareTo(BigDecimal.valueOf(minValue)) != -1;
		} else if (value instanceof BigInteger) {
			return ((BigInteger) value).compareTo(BigInteger.valueOf(minValue)) != -1;
		} else {
			return value.longValue() >= minValue;
		}
	}

	public static boolean validateMax(Number value, long max) {
        if (value == null) {
            return true;
        } else if (value instanceof BigDecimal) {
            return ((BigDecimal) value).compareTo(BigDecimal.valueOf(max)) != 1;
        } else if (value instanceof BigInteger) {
            return ((BigInteger) value).compareTo(BigInteger.valueOf(max)) != 1;
        } else {
            return value.longValue() <= max;
        }
	}

	public static boolean validateRange(Number value, long min, long max) {
        if (value == null) {
            return true;
        } else if (value instanceof BigDecimal) {
            return (((BigDecimal) value).compareTo(BigDecimal.valueOf(max)) != 1) && (((BigDecimal) value).compareTo(BigDecimal.valueOf(max)) != -1);
        } else if (value instanceof BigInteger) {
            return (((BigInteger) value).compareTo(BigInteger.valueOf(max)) != 1) && (((BigInteger) value).compareTo(BigInteger.valueOf(max)) != -1);
        } else {
            return value.longValue() <= max && value.longValue() >= min;
        }
	}
	
	public static boolean validateUrl(String url, String protocol, String host, int port, String regexp, String flags) {
		throw new RuntimeException("Not yet implemented!");
	}
	
	public static boolean validateEmail(String email) {
		return EmailValidationUtils.isValid(email);
	}
}
