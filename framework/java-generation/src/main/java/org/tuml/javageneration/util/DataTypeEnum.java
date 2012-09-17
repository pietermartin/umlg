package org.tuml.javageneration.util;

import org.eclipse.uml2.uml.DataType;
import org.opaeum.java.metamodel.OJPathName;

public enum DataTypeEnum {
	DateTime("java.util.Date"), Email("java.lang.String");
	private OJPathName pathName;

	private DataTypeEnum(String s) {
		this.pathName = new OJPathName(s);
	}
	
	
	public static OJPathName fromDataType(DataType dataType) {
		if (dataType.getName().equals(DateTime.name())) {
			return DateTime.getPathName();
		} else if (dataType.getName().equals(Email.name())) {
			return Email.getPathName();
		} else {
			throw new IllegalStateException("Unkown data type");
		}
	}

	public OJPathName getPathName() {
		return pathName;
	}
}
