package org.umlg.javageneration.validation;

import org.umlg.java.metamodel.OJPathName;


public class MinLong implements Validation {

	private Number min;

	public Number getMin() {
		return min;
	}

	public MinLong(Number min) {
		super();
		this.min = min;
	}

	@Override
	public String toStringForMethod() {
		return getMin().toString();
	}

	@Override
	public String toNewRuntimeTumlValidation() {
		return "new MinLong(" + min.toString() + ")";
	}
	
	@Override
	public OJPathName getPathName() {
		return new OJPathName("org.umlg.runtime.validation.MinLong");
	}
	
	@Override
	public String toJson() {
		return "\\\"min\\\": " + String.valueOf(min);
	}


}
