package org.umlg.javageneration.util;

import org.eclipse.uml2.uml.DataType;
import org.umlg.java.metamodel.OJPathName;

public enum DataTypeEnum {
    DateTime("org.joda.time.DateTime", "DataTypeEnum.DateTime"),
    Date("org.joda.time.LocalDate", "DataTypeEnum.Date"),
    Time("org.joda.time.LocalTime", "DataTypeEnum.Time"),
    InternationalPhoneNumber("java.lang.String", "DataTypeEnum.InternationalPhoneNumber"),
    LocalPhoneNumber("java.lang.String", "DataTypeEnum.LocalPhoneNumber"),
    Email("java.lang.String", "DataTypeEnum.Email"),
    Video("byte[]", "DataTypeEnum.Video"),
    Audio("byte[]", "DataTypeEnum.Audio"),
    Image("byte[]", "DataTypeEnum.Image");
	private OJPathName pathName;
	private String initExpression;

	private DataTypeEnum(String s, String initExpression) {
		this.pathName = new OJPathName(s);
		this.initExpression = initExpression;
	}

	public String getInitExpression() {
		return initExpression;
	}

	public static OJPathName getPathNameFromDataType(DataType dataType) {
		return fromDataType(dataType).getPathName();
	}

	public static DataTypeEnum fromDataType(DataType dataType) {
		if (dataType.getName().equals(DateTime.name())) {
			return DateTime;
		} else if (dataType.getName().equals(Date.name())) {
			return Date;
		} else if (dataType.getName().equals(Time.name())) {
			return Time;
		} else if (dataType.getName().equals(InternationalPhoneNumber.name())) {
			return InternationalPhoneNumber;
		} else if (dataType.getName().equals(LocalPhoneNumber.name())) {
			return LocalPhoneNumber;
		} else if (dataType.getName().equals(Email.name())) {
			return Email;
		} else if (dataType.getName().equals(Video.name())) {
			return Video;
		} else if (dataType.getName().equals(Audio.name())) {
			return Audio;
		} else if (dataType.getName().equals(Image.name())) {
			return Image;
		} else {
			return null;
		}
	}

	public OJPathName getPathName() {
		return pathName;
	}

	public boolean isDateTime() {
		return this == DateTime;
	}

	public boolean isDate() {
		return this == Date;
	}

	public boolean isTime() {
		return this == Time;
	}

	public boolean isInternationalPhoneNumber() {
		return this == InternationalPhoneNumber;
	}

	public boolean isLocalPhoneNumber() {
		return this == LocalPhoneNumber;
	}

	public boolean isEmail() {
		return this == Email;
	}

	public boolean isVideo() {
		return this == Video;
	}

	public boolean isAudio() {
		return this == Audio;
	}

	public boolean isImage() {
		return this == Image;
	}

}
