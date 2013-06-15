package org.umlg.javageneration.validation;

import org.umlg.java.metamodel.OJPathName;


public class DateTime implements Validation {

	public DateTime() {
		super();
	}

	@Override
	public String toStringForMethod() {
		return "";
	}

	@Override
	public String toNewRuntimeTumlValidation() {
		return "new DateTimeValidation()";
	}
	
	@Override
	public OJPathName getPathName() {
		return new OJPathName("org.umlg.runtime.validation.DateTimeValidation");
	}
	
	@Override
	public String toJson() {
		return "\\\"dateTime\\\": {}";
	}

}
