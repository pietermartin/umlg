package org.tuml.javageneration.validation;

import org.opaeum.java.metamodel.OJPathName;


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
		return new OJPathName("org.tuml.runtime.validation.DateTimeValidation");
	}
	
	@Override
	public String toJson() {
		return "\\\"dateTime\\\": {}";
	}

}
