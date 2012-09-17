package org.tuml.runtime.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.DatatypeConverter;

//TODO make this threadvar or something
/**
 * DatatypeConverter uses ISO8610
 */
public class TinkerFormatter {

	// THe dashes instead of : in the hours minutes and seconds is for orientdb
	// which does not like :
	// private static final String TINKER_DATE_FORMAT =
	// "yyyy.MM.dd G 'at' HH-mm-ss z";

	public static Date parse(String date) {
		// try {
		return DatatypeConverter.parseDateTime(date).getTime();
		// return new SimpleDateFormat(TINKER_DATE_FORMAT).parse(date);
		// } catch (ParseException e) {
		// throw new RuntimeException(e);
		// }
	}

	public static String format(Date date) {
		Calendar c = GregorianCalendar.getInstance();
		c.setTime(date);
		return DatatypeConverter.printDateTime(c);
		// return new SimpleDateFormat(TINKER_DATE_FORMAT).format(date);
	}
}
