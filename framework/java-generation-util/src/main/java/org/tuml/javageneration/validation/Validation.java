package org.tuml.javageneration.validation;

import org.opaeum.java.metamodel.OJPathName;

public interface Validation {

	String toStringForMethod();
	String toNewRuntimeTumlValidation();
	OJPathName getPathName();
	String toJson();

}
