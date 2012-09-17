package org.tuml.javageneration.util;

import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.internal.operations.DataTypeOperations;

public class TumlDataTypeOperation extends DataTypeOperations {

	public static boolean isDate(DataType dataType) {
		return dataType.getQualifiedName().equals("tumldatatypes::DateTime");
	}

	public static boolean isEmail(DataType dataType) {
		return dataType.getQualifiedName().equals("tumldatatypes::Email");
	}
}
