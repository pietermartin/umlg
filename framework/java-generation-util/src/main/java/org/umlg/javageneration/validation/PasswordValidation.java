package org.umlg.javageneration.validation;

import org.umlg.java.metamodel.OJPathName;


public class PasswordValidation implements Validation {

	public PasswordValidation() {
		super();
	}

	@Override
	public String toStringForMethod() {
		return "";
	}

	@Override
	public String toNewRuntimeTumlValidation() {
		return "new PasswordValidation()";
	}
	
	@Override
	public OJPathName getPathName() {
		return new OJPathName("org.umlg.runtime.validation.PasswordValidation");
	}
	
	@Override
	public String toJson() {
		return "\\\"password\\\": {}";
	}

}
