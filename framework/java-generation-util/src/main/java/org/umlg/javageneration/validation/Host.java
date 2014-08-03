package org.umlg.javageneration.validation;

import org.umlg.java.metamodel.OJPathName;


public class Host implements Validation {

	public Host() {
		super();
	}

	@Override
	public String toStringForMethod() {
		return "";
	}

	@Override
	public String toNewRuntimeTumlValidation() {
		return "new Host()";
	}
	
	@Override
	public OJPathName getPathName() {
		return new OJPathName("org.umlg.runtime.validation.Host");
	}
	
	@Override
	public String toJson() {
		return "\\\"host\\\": {}";
	}

}
