package org.tuml.runtime.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//TODO make this threadvar or something
public class TinkerFormatter {

	//THe dashes instead of : in the hours minutes and seconds is for orientdb which does not like :
	private static final String TINKER_DATE_FORMAT = "yyyy.MM.dd G 'at' HH-mm-ss z";

	public static Date parse(String date) {
		try {
			return new SimpleDateFormat(TINKER_DATE_FORMAT).parse(date);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String format(Date date) {
		return new SimpleDateFormat(TINKER_DATE_FORMAT).format(date);
	}

}
