package org.tuml.javageneration.validation;

import org.tuml.java.metamodel.OJPathName;

public interface Validation {

	String toStringForMethod();
	String toNewRuntimeTumlValidation();
	OJPathName getPathName();
	String toJson();

}
