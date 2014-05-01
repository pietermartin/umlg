package org.umlg.javageneration.validation;

import org.umlg.java.metamodel.OJPathName;


public class MinDouble implements Validation {

	private Number min;

	public Number getMin() {
		return min;
	}

	public MinDouble(Number min) {
		super();
		this.min = min;
	}

	@Override
	public String toStringForMethod() {
		return getMin().toString() + "D";
	}

	@Override
	public String toNewRuntimeTumlValidation() {
		return "new MinDouble(" + min.toString() + "D)";
	}
	
	@Override
	public OJPathName getPathName() {
		return new OJPathName("org.umlg.runtime.validation.MinDouble");
	}
	
	@Override
	public String toJson() {
		return "\\\"min\\\": " + String.valueOf(min);
	}


}
