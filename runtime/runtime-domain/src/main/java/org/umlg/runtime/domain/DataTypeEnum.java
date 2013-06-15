package org.umlg.runtime.domain;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public enum DataTypeEnum {

    DateTime(DateTime.class),
    Date(LocalDate.class),
    Time(LocalTime.class),
    InternationalPhoneNumber(String.class),
    LocalPhoneNumber(String.class),
    Email(String.class),
    Video(Object.class),
    Audio(Object.class),
    Image(Object.class);

    Class type;

    private DataTypeEnum(Class type) {
        this.type = type;
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

    public Class getType() {
        return this.type;
    }

}
