package org.umlg.javageneration.validation;

import org.umlg.java.metamodel.OJPathName;


public class MinUnlimitedNatural implements Validation {

	private Integer min;

	public Number getMin() {
		return min;
	}

	public MinUnlimitedNatural(Integer min) {
		super();
		this.min = min;
	}

	@Override
	public String toStringForMethod() {
		return getMin().toString();
	}

	@Override
	public String toNewRuntimeTumlValidation() {
		return "new MinUnlimitedNatural(" + min.toString() + ")";
	}
	
	@Override
	public OJPathName getPathName() {
		return new OJPathName("org.umlg.runtime.validation.MinUnlimitedNatural");
	}
	
	@Override
	public String toJson() {
		return "\\\"min\\\": " + String.valueOf(min);
	}


}
