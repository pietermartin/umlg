package org.tuml.runtime.domain;

public enum DataTypeEnum {

	DateTime, Date, Time, InternationalPhoneNumber, LocalPhoneNumber, Email, Video, Audio, Image;

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
}
