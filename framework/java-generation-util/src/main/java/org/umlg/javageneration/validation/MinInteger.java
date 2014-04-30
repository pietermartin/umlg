package org.umlg.javageneration.validation;

import org.umlg.java.metamodel.OJPathName;


public class MinInteger implements Validation {

	private Number min;

	public Number getMin() {
		return min;
	}

	public MinInteger(Number min) {
		super();
		this.min = min;
	}

	@Override
	public String toStringForMethod() {
		return getMin().toString();
	}

	@Override
	public String toNewRuntimeTumlValidation() {
		return "new MinInteger(" + min.toString() + ")";
	}
	
	@Override
	public OJPathName getPathName() {
		return new OJPathName("org.umlg.runtime.validation.MinInteger");
	}
	
	@Override
	public String toJson() {
		return "\\\"min\\\": " + String.valueOf(min);
	}


}
