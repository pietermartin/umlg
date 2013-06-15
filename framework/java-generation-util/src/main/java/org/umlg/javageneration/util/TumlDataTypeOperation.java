package org.umlg.javageneration.util;

import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.internal.operations.DataTypeOperations;

public class TumlDataTypeOperation extends DataTypeOperations {

	public static boolean isDate(DataType dataType) {
		return dataType.getQualifiedName().equals("tumldatatypes::Date");
	}

	public static boolean isDateTime(DataType dataType) {
		return dataType.getQualifiedName().equals("tumldatatypes::DateTime");
	}

	public static boolean isTime(DataType dataType) {
		return dataType.getQualifiedName().equals("tumldatatypes::Time");
	}
	
	public static boolean isEmail(DataType dataType) {
		return dataType.getQualifiedName().equals("tumldatatypes::Email");
	}

	public static boolean isInternationalPhoneNumber(DataType dataType) {
		return dataType.getQualifiedName().equals("tumldatatypes::InternationalPhoneNumber");
	}

	public static boolean isLocalPhoneNumber(DataType dataType) {
		return dataType.getQualifiedName().equals("tumldatatypes::LocalPhoneNumber");
	}

	public static boolean isVideo(DataType dataType) {
		return dataType.getQualifiedName().equals("tumldatatypes::Video");
	}

	public static boolean isAudio(DataType dataType) {
		return dataType.getQualifiedName().equals("tumldatatypes::Audio");
	}

	public static boolean isImage(DataType dataType) {
		return dataType.getQualifiedName().equals("tumldatatypes::Image");
	}

	public static DataTypeEnum getDataTypeEnum(DataType type) {
		return DataTypeEnum.fromDataType(type);
	}

}
