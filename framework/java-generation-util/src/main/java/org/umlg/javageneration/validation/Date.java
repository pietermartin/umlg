package org.umlg.javageneration.validation;

import org.umlg.java.metamodel.OJPathName;


public class Date implements Validation {

	public Date() {
		super();
	}

	@Override
	public String toStringForMethod() {
		return "";
	}

	@Override
	public String toNewRuntimeTumlValidation() {
		return "new DateValidation()";
	}
	
	@Override
	public OJPathName getPathName() {
		return new OJPathName("org.umlg.runtime.validation.DateValidation");
	}
	
	@Override
	public String toJson() {
		return "\\\"date\\\": {}";
	}

}
