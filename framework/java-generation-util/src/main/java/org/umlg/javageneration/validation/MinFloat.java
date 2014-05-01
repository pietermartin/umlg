package org.umlg.javageneration.validation;

import org.umlg.java.metamodel.OJPathName;


public class MinFloat implements Validation {

	private Number min;

	public Number getMin() {
		return min;
	}

	public MinFloat(Number min) {
		super();
		this.min = min;
	}

	@Override
	public String toStringForMethod() {
		return getMin().toString() + "F";
	}

	@Override
	public String toNewRuntimeTumlValidation() {
		return "new MinFloat(" + min.toString() + "F)";
	}
	
	@Override
	public OJPathName getPathName() {
		return new OJPathName("org.umlg.runtime.validation.MinFloat");
	}
	
	@Override
	public String toJson() {
		return "\\\"min\\\": " + String.valueOf(min);
	}


}
