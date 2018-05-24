package org.umlg.javageneration.util;

import org.eclipse.uml2.uml.DataType;
import org.umlg.java.metamodel.OJPathName;

public enum DataTypeEnum {
    DateTime("java.time.LocalDateTime", "DataTypeEnum.DateTime", "LocalDateTime"),
    Date("java.time.LocalDate", "DataTypeEnum.Date", "LocalDate"),
    Time("java.time.LocalTime", "DataTypeEnum.Time", "LocalTime"),
    InternationalPhoneNumber("java.lang.String", "DataTypeEnum.InternationalPhoneNumber", "String"),
    LocalPhoneNumber("java.lang.String", "DataTypeEnum.LocalPhoneNumber", "String"),
    Email("java.lang.String", "DataTypeEnum.Email", "String"),
    Video("byte[]", "DataTypeEnum.Video", "byte[]"),
    Audio("byte[]", "DataTypeEnum.Audio", "byte[]"),
    Image("byte[]", "DataTypeEnum.Image", "byte[]"),
    Host("java.lang.String", "DataTypeEnum.Host", "String"),
    QuartzCron("java.lang.String", "DataTypeEnum.QuartzCron", "String"),
    UnixCron("java.lang.String", "DataTypeEnum.UnixCron", "String"),
    Password("org.umlg.runtime.types.Password", "DataTypeEnum.Password", "byte[]"),
    UnsecurePassword("java.lang.String", "DataTypeEnum.UnsecurePassword", "String"),
    ByteArray("byte[]", "DataTypeEnum.ByteArray", "byte[]");

    private OJPathName pathName;
    private String initExpression;
    private String persistentType;

    DataTypeEnum(String s, String initExpression, String persistentType) {
        this.pathName = new OJPathName(s);
        this.initExpression = initExpression;
        this.persistentType = persistentType;
    }

    public String getPersistentType() {
        return persistentType;
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
        } else if (dataType.getName().equals(Host.name())) {
            return Host;
        } else if (dataType.getName().equals(QuartzCron.name())) {
            return QuartzCron;
        } else if (dataType.getName().equals(UnixCron.name())) {
            return UnixCron;
        } else if (dataType.getName().equals(Password.name())) {
            return Password;
        } else if (dataType.getName().equals(UnsecurePassword.name())) {
            return UnsecurePassword;
        } else if (dataType.getName().equals(ByteArray.name())) {
            return ByteArray;
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

    public boolean isHost() {
        return this == Host;
    }

    public boolean isQuartzCron() {
        return this == QuartzCron;
    }

    public boolean isUnixCron() {
        return this == UnixCron;
    }

    public boolean isPassword() {
        return this == Password;
    }

    public boolean isUnsecurePassword() {
        return this == UnsecurePassword;
    }

    public boolean isByteArray() {
        return this == ByteArray;
    }

}
