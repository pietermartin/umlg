package org.umlg.javageneration.ocl.util;

public class UmlgOclUtil {

	public final static String removeVariableInit(String v) {
		return v.substring(0, v.indexOf("=")).trim();
	}

}
