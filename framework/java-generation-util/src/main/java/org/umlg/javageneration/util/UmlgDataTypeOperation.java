package org.umlg.javageneration.util;

import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.internal.operations.DataTypeOperations;

public class UmlgDataTypeOperation extends DataTypeOperations {

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

    public static boolean isHost(DataType dataType) {
        return dataType.getQualifiedName().equals("umlgdatatypes::Host");
    }

    public static boolean isQuartzCron(DataType dataType) {
        return dataType.getQualifiedName().equals("umlgdatatypes::QuartzCron");
    }

    public static boolean isUnixCron(DataType dataType) {
        return dataType.getQualifiedName().equals("umlgdatatypes::UnixCron");
    }

    public static boolean isPassword(DataType dataType) {
        return dataType.getQualifiedName().equals("umlgdatatypes::Password");
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
