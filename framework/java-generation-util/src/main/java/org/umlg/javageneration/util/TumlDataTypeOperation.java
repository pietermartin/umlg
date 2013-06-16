package org.umlg.javageneration.util;

import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.internal.operations.DataTypeOperations;

public class TumlDataTypeOperation extends DataTypeOperations {

	public static boolean isDate(DataType dataType) {
		return dataType.getQualifiedName().equals("umlgdatatypes::Date");
	}

	public static boolean isDateTime(DataType dataType) {
		return dataType.getQualifiedName().equals("umlgdatatypes::DateTime");
	}

	public static boolean isTime(DataType dataType) {
		return dataType.getQualifiedName().equals("umlgdatatypes::Time");
	}
	
	public static boolean isEmail(DataType dataType) {
		return dataType.getQualifiedName().equals("umlgdatatypes::Email");
	}

	public static boolean isInternationalPhoneNumber(DataType dataType) {
		return dataType.getQualifiedName().equals("umlgdatatypes::InternationalPhoneNumber");
	}

	public static boolean isLocalPhoneNumber(DataType dataType) {
		return dataType.getQualifiedName().equals("umlgdatatypes::LocalPhoneNumber");
	}

	public static boolean isVideo(DataType dataType) {
		return dataType.getQualifiedName().equals("umlgdatatypes::Video");
	}

	public static boolean isAudio(DataType dataType) {
		return dataType.getQualifiedName().equals("umlgdatatypes::Audio");
	}

	public static boolean isImage(DataType dataType) {
		return dataType.getQualifiedName().equals("umlgdatatypes::Image");
	}

	public static DataTypeEnum getDataTypeEnum(DataType type) {
		return DataTypeEnum.fromDataType(type);
	}

}
