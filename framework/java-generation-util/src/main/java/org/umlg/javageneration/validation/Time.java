package org.umlg.javageneration.validation;

import org.umlg.java.metamodel.OJPathName;


public class Time implements Validation {

	public Time() {
		super();
	}

	@Override
	public String toStringForMethod() {
		return "";
	}

	@Override
	public String toNewRuntimeTumlValidation() {
		return "new TimeValidation()";
	}
	
	@Override
	public OJPathName getPathName() {
		return new OJPathName("org.umlg.runtime.validation.TimeValidation");
	}
	
	@Override
	public String toJson() {
		return "\\\"time\\\": {}";
	}

}
