package org.tuml.javageneration.util;

import org.eclipse.uml2.uml.DataType;
import org.opaeum.java.metamodel.OJPathName;

public enum DataTypeEnum {
	DateTime("org.joda.time.DateTime"), Date("org.joda.time.LocalDate"), Time("org.joda.time.LocalTime"), InternationalPhoneNumber("java.lang.String"),
	LocalPhoneNumber("java.lang.String"), Email("java.lang.String"), Video("byte[]"), Audio("byte[]"), Image("byte[]");
	private OJPathName pathName;

	private DataTypeEnum(String s) {
		this.pathName = new OJPathName(s);
	}
	
	
	public static OJPathName fromDataType(DataType dataType) {
		if (dataType.getName().equals(DateTime.name())) {
			return DateTime.getPathName();
		} else if (dataType.getName().equals(Date.name())) {
			return Date.getPathName();
		} else if (dataType.getName().equals(Time.name())) {
			return Time.getPathName();
		} else if (dataType.getName().equals(InternationalPhoneNumber.name())) {
			return InternationalPhoneNumber.getPathName();
		} else if (dataType.getName().equals(LocalPhoneNumber.name())) {
			return LocalPhoneNumber.getPathName();
		} else if (dataType.getName().equals(Email.name())) {
			return Email.getPathName();
		} else if (dataType.getName().equals(Video.name())) {
			return Video.getPathName();
		} else if (dataType.getName().equals(Audio.name())) {
			return Audio.getPathName();
		} else if (dataType.getName().equals(Image.name())) {
			return Image.getPathName();
		} else {
			throw new IllegalStateException(String.format("Unkown data type %s", dataType.getName()));
		}
	}

	public OJPathName getPathName() {
		return pathName;
	}
}
