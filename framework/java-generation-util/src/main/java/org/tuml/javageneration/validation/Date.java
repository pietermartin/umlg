package org.tuml.javageneration.validation;

import org.opaeum.java.metamodel.OJPathName;


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
		return new OJPathName("org.tuml.runtime.validation.DateValidation");
	}
	
	@Override
	public String toJson() {
		return "\\\"date\\\": {}";
	}

}
