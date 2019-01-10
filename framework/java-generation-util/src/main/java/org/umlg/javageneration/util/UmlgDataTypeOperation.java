package org.umlg.javageneration.util;

import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.internal.operations.DataTypeOperations;

public class UmlgDataTypeOperation extends DataTypeOperations {

	public static boolean isDate(DataType dataType) {
		return dataType.getQualifiedName().endsWith("umlgdatatypes::Date");
	}

	public static boolean isDateTime(DataType dataType) {
		return dataType.getQualifiedName().endsWith("umlgdatatypes::DateTime");
	}

	public static boolean isTime(DataType dataType) {
		return dataType.getQualifiedName().endsWith("umlgdatatypes::Time");
	}
	
	public static boolean isEmail(DataType dataType) {
		return dataType.getQualifiedName().endsWith("umlgdatatypes::Email");
	}

    public static boolean isHost(DataType dataType) {
        return dataType.getQualifiedName().endsWith("umlgdatatypes::Host");
    }

    public static boolean isQuartzCron(DataType dataType) {
        return dataType.getQualifiedName().endsWith("umlgdatatypes::QuartzCron");
    }

    public static boolean isUnixCron(DataType dataType) {
        return dataType.getQualifiedName().endsWith("umlgdatatypes::UnixCron");
    }

    public static boolean isPassword(DataType dataType) {
        return dataType.getQualifiedName().endsWith("umlgdatatypes::Password");
    }

    public static boolean isInternationalPhoneNumber(DataType dataType) {
		return dataType.getQualifiedName().endsWith("umlgdatatypes::InternationalPhoneNumber");
	}

	public static boolean isLocalPhoneNumber(DataType dataType) {
		return dataType.getQualifiedName().endsWith("umlgdatatypes::LocalPhoneNumber");
	}

	public static boolean isVideo(DataType dataType) {
		return dataType.getQualifiedName().endsWith("umlgdatatypes::Video");
	}

	public static boolean isAudio(DataType dataType) {
		return dataType.getQualifiedName().endsWith("umlgdatatypes::Audio");
	}

	public static boolean isImage(DataType dataType) {
		return dataType.getQualifiedName().endsWith("umlgdatatypes::Image");
	}

	public static DataTypeEnum getDataTypeEnum(DataType type) {
		return DataTypeEnum.fromDataType(type);
	}

}
