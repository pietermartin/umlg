package org.tuml.javageneration.ocl.util;

public class TumlOclUtil {

	public final static String removeVariableInit(String v) {
		return v.substring(0, v.indexOf("=")).trim();
	}

}
