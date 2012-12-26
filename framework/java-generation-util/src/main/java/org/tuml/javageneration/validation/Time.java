package org.tuml.javageneration.validation;

import org.tuml.java.metamodel.OJPathName;


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
		return new OJPathName("org.tuml.runtime.validation.TimeValidation");
	}
	
	@Override
	public String toJson() {
		return "\\\"time\\\": {}";
	}

}
