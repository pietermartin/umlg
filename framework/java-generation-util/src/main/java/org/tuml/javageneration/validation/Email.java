package org.tuml.javageneration.validation;

import org.opaeum.java.metamodel.OJPathName;


public class Email implements Validation {

	public Email() {
		super();
	}

	@Override
	public String toStringForMethod() {
		return "";
	}

	@Override
	public String toNewRuntimeTumlValidation() {
		return "new Email()";
	}
	
	@Override
	public OJPathName getPathName() {
		return new OJPathName("org.tuml.runtime.validation.Email");
	}
	
	@Override
	public String toJson() {
		return "\\\"email\\\": {}";
	}

}
