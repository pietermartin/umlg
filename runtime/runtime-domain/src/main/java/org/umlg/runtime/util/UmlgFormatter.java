package org.umlg.runtime.util;

import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.umlg.runtime.domain.DataTypeEnum;

//TODO make this threadvar or something

/**
 * DatatypeConverter uses ISO8610
 */
public class UmlgFormatter {

    // THe dashes instead of : in the hours minutes and seconds is for orientdb
    // which does not like :
    // private static final String TINKER_DATE_FORMAT =
    // "yyyy.MM.dd G 'at' HH-mm-ss z";

    public static LocalDate parseDate(String date) {
        if (date != null && !date.isEmpty()) {
            return LocalDate.parse(date);
        } else {
            return null;
        }
    }

    public static DateTime parseDateTime(String dateTime) {
        if (dateTime != null && !dateTime.isEmpty()) {
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            return fmt.parseDateTime(dateTime);
        } else {
            return null;
        }
    }

    public static LocalTime parseTime(String time) {
        if (time != null && !time.isEmpty()) {
            return LocalTime.parse(time);
        } else {
            return null;
        }
    }

    //TODO use stereotypes on model to specify the format
    public static String format(DateTime dateTime) {
        if (dateTime != null) {
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            return fmt.print(dateTime);
        } else {
            return "";
        }
    }

    //TODO use stereotypes on model to specify the format
    public static String format(LocalDate date) {
        if (date != null) {
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
            return fmt.print(date);
        } else {
            return "";
        }
    }

    //TODO use stereotypes on model to specify the format
    public static String format(LocalTime time) {
        if (time != null) {
            DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
            return fmt.print(time);
        } else {
            return "";
        }
    }

    public static Long formatToPersist(DateTime dateTime) {
        if (dateTime != null) {
            return dateTime.getMillis();
        } else {
            return 0L;
        }
    }

    public static Long formatToPersist(LocalDate date) {
        if (date != null) {
            return date.toDateTimeAtStartOfDay().getMillis();
        } else {
            return 0L;
        }
    }

    public static String formatToPersist(LocalTime time) {
        if (time != null) {
            return ISODateTimeFormat.time().print(time);
        } else {
            return "";
        }
    }

    public static DateTime parseDateTimeFromPersist(long dateTime) {
        return new DateTime(dateTime);
    }

    public static LocalDate parseDateFromPersist(long date) {
        return new LocalDate(date);
    }

    public static LocalTime parseTimeFromPersist(String time) {
        if (time != null && !time.isEmpty()) {
            return LocalTime.parse(time);
        } else {
            return null;
        }
    }


    public static String encode(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    public static byte[] decode(String base64String) {
        return Base64.decodeBase64(base64String);
    }

    public static Object format(DataTypeEnum dataTypeEnum, Object o) {
        switch (dataTypeEnum) {
            case DateTime:
                return formatToPersist((DateTime) o);
            case Date:
                return formatToPersist((LocalDate) o);
            case Time:
                return formatToPersist((LocalTime) o);
            case InternationalPhoneNumber:
                return o;
            case LocalPhoneNumber:
                return o;
            case Email:
                return o;
            case Host:
                return o;
            case QuartzCron:
                return o;
            case UnixCron:
                return o;
            case Password:
                return o;
            case ByteArray:
                return o;
//            case Video:
//                return format((DateTime)o);
//            case Audio:
//                return format((DateTime)o);
//            case Image:
//                return format((DateTime)o);
            default:
                throw new IllegalStateException("Unhandled dataTypeEnum " + dataTypeEnum.name());
        }
    }

    public static <E> E parse(DataTypeEnum dataTypeEnum, Object s) {
        switch (dataTypeEnum) {
            case DateTime:
                return (E)parseDateTimeFromPersist((Long)s);
            case Date:
                return (E)parseDateFromPersist((Long)s);
            case Time:
                return (E)parseTimeFromPersist((String)s);
            case InternationalPhoneNumber:
                return (E)s;
            case LocalPhoneNumber:
                return (E)s;
            case Email:
                return (E)s;
            case Host:
                return (E)s;
            case QuartzCron:
                return (E)s;
            case UnixCron:
                return (E)s;
            case Password:
                return (E)s;
            case ByteArray:
                return (E)s;
//            case Video:
//                return format((DateTime)o);
//            case Audio:
//                return format((DateTime)o);
//            case Image:
//                return format((DateTime)o);
            default:
                throw new IllegalStateException("Unhandled dataTypeEnum " + dataTypeEnum.name());
        }
    }
}
