package org.umlg.javageneration.ocl.util;

public class UmlgOclUtil {

	public final static String removeVariableInit(String v) {
		return v.substring(0, v.indexOf("=")).trim();
	}

	public static String extractVariable(String v) {
		String tmp = v.substring(v.indexOf(" ") + 1);
		return tmp.substring(0, tmp.indexOf(" ") + 1);
	}
}
