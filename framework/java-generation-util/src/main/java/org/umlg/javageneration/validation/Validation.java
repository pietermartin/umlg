package org.umlg.javageneration.validation;

import org.umlg.java.metamodel.OJPathName;

public interface Validation {

	String toStringForMethod();
	String toNewRuntimeTumlValidation();
	OJPathName getPathName();
	String toJson();

}
