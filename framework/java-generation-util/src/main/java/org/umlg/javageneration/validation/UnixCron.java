package org.umlg.javageneration.validation;

import org.umlg.java.metamodel.OJPathName;


public class UnixCron implements Validation {

	public UnixCron() {
		super();
	}

	@Override
	public String toStringForMethod() {
		return "";
	}

	@Override
	public String toNewRuntimeTumlValidation() {
		return "new UnixCron()";
	}
	
	@Override
	public OJPathName getPathName() {
		return new OJPathName("org.umlg.runtime.validation.UnixCron");
	}
	
	@Override
	public String toJson() {
		return "\\\"unixCron\\\": {}";
	}

}
