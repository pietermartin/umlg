package org.umlg.javageneration.validation;

import org.umlg.java.metamodel.OJPathName;


public class QuartzCron implements Validation {

	public QuartzCron() {
		super();
	}

	@Override
	public String toStringForMethod() {
		return "";
	}

	@Override
	public String toNewRuntimeTumlValidation() {
		return "new QuartzCron()";
	}
	
	@Override
	public OJPathName getPathName() {
		return new OJPathName("org.umlg.runtime.validation.QuartzCron");
	}
	
	@Override
	public String toJson() {
		return "\\\"quartzCron\\\": {}";
	}

}
