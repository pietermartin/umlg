package org.umlg.javageneration.validation;

import org.umlg.java.metamodel.OJPathName;


public class MinReal implements Validation {

	private Double min;

	public Number getMin() {
		return min;
	}

	public MinReal(Double min) {
		super();
		this.min = min;
	}

	@Override
	public String toStringForMethod() {
		return getMin().toString();
	}

	@Override
	public String toNewRuntimeTumlValidation() {
		return "new MinReal(" + min.toString() + ")";
	}
	
	@Override
	public OJPathName getPathName() {
		return new OJPathName("org.umlg.runtime.validation.MinReal");
	}
	
	@Override
	public String toJson() {
		return "\\\"min\\\": " + String.valueOf(min);
	}


}
