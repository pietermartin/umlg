package org.test.restlet;

import org.joda.time.DateTime;

public class Test {
	public static void main(String[] args) {
		DateTime dateTime = new DateTime();
		System.out.println(dateTime.toString());
		DateTime d = new DateTime("2012-09-03T04:32:00");
		System.out.println(d);
	}
}
