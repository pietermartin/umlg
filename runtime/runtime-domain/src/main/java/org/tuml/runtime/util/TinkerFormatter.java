package org.tuml.runtime.util;

import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

//TODO make this threadvar or something
/**
 * DatatypeConverter uses ISO8610
 */
public class TinkerFormatter {

	// THe dashes instead of : in the hours minutes and seconds is for orientdb
	// which does not like :
	// private static final String TINKER_DATE_FORMAT =
	// "yyyy.MM.dd G 'at' HH-mm-ss z";

	public static LocalDate parseDate(String date) {
		if (date != null && !date.isEmpty()) {
			return LocalDate.parse(date);
		} else {
			return null;
		}
	}

	public static DateTime parseDateTime(String dateTime) {
		if (dateTime != null && !dateTime.isEmpty()) {
			return DateTime.parse(dateTime);
		} else {
			return null;
		}
	}

	public static LocalTime parseTime(String time) {
		if (time != null && !time.isEmpty()) {
			return LocalTime.parse(time);
		} else {
			return null;
		}
	}

	public static String format(DateTime dateTime) {
		if (dateTime != null) {
			return dateTime.toString();
		} else {
			return "";
		}
	}

	public static String format(LocalDate date) {
		if (date != null) {
			return date.toString();
		} else {
			return "";
		}
	}

	public static String format(LocalTime time) {
		if (time != null) {
			return time.toString();
		} else {
			return "";
		}
	}
	
	public static String encode(byte[] bytes) {
		return Base64.encodeBase64String(bytes); 
	}
	
	public static byte[] decode(String base64String) {
		return Base64.decodeBase64(base64String);
	}
}
