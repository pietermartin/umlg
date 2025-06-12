package org.umlg.runtime.util;

import org.apache.commons.lang3.ArrayUtils;
import org.umlg.runtime.domain.DataTypeEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

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

    public static LocalDateTime parseDateTime(String dateTime) {
        if (dateTime != null && !dateTime.isEmpty()) {
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(dateTime, formatter);
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
    public static String format(LocalDateTime dateTime) {
        if (dateTime != null) {
            java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return fmt.format(dateTime);
        } else {
            return "";
        }
    }

    //TODO use stereotypes on model to specify the format
    public static String format(LocalDate date) {
        if (date != null) {
            java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return fmt.format(date);
        } else {
            return "";
        }
    }

    //TODO use stereotypes on model to specify the format
    public static String format(LocalTime time) {
        if (time != null) {
            java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("HH:mm");
            return fmt.format(time);
        } else {
            return "";
        }
    }

//    public static Long formatToPersist(LocalDateTime dateTime) {
//        if (dateTime != null) {
//            return dateTime.getMillis();
//        } else {
//            return 0L;
//        }
//    }
//
//    public static Long formatToPersist(LocalDate date) {
//        if (date != null) {
//            return date.toDateTimeAtStartOfDay().getMillis();
//        } else {
//            return 0L;
//        }
//    }
//
//    public static String formatToPersist(LocalTime time) {
//        if (time != null) {
//            return ISODateTimeFormat.time().print(time);
//        } else {
//            return "";
//        }
//    }
//
//    public static LocalDateTime parseDateTimeFromPersist(long dateTime) {
//        return new LocalDateTime(dateTime);
//    }
//
//    public static LocalDate parseDateFromPersist(long date) {
//        return new LocalDate(date);
//    }
//
//    public static LocalTime parseTimeFromPersist(String time) {
//        if (time != null && !time.isEmpty()) {
//            return LocalTime.parse(time);
//        } else {
//            return null;
//        }
//    }
//
//
//    public static String encode(byte[] bytes) {
//        return Base64.encodeBase64String(bytes);
//    }
//
//    public static byte[] decode(String base64String) {
//        return Base64.decodeBase64(base64String);
//    }

    public static Object format(DataTypeEnum dataTypeEnum, Object o) {
        switch (dataTypeEnum) {
            case DateTime:
//                return formatToPersist((LocalDateTime) o);
            case Date:
//                return formatToPersist((LocalDate) o);
            case Time:
//                return formatToPersist((LocalTime) o);
                return o;
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
            case UnsecurePassword:
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
//                return (E) parseDateTimeFromPersist((Long) s);
            case Date:
//                return (E) parseDateFromPersist((Long) s);
            case Time:
//                return (E) parseTimeFromPersist((String) s);
                return (E) s;
            case InternationalPhoneNumber:
                return (E) s;
            case LocalPhoneNumber:
                return (E) s;
            case Email:
                return (E) s;
            case Host:
                return (E) s;
            case QuartzCron:
                return (E) s;
            case UnixCron:
                return (E) s;
            case Password:
                //this is a f up, need the salt also
//                byte[] salt = v.<byte[]>property(persistentName + SALT).value();
//                this.encryptedPassword = v.<byte[]>property(persistentName).value();
//                Password password = new Password();
//                password.loadFromVertex(this.vertex, getPersistentName());
//                result = (E) password;
                return (E) s;
            case UnsecurePassword:
                return (E) s;
            case ByteArray:
                return (E) s;
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

    public static <E> Collection<E> convertFromArray(DataTypeEnum dataTypeEnum, Object array) {
        Collection<E> result = new ArrayList<>();
        switch (dataTypeEnum) {
            case DateTime:
                LocalDateTime[] localDateTimeArray = (LocalDateTime[]) array;
                for (LocalDateTime l : localDateTimeArray) {
                    result.add((E) l);
                }
                break;
            case Date:
                LocalDate[] localDateArray = (LocalDate[]) array;
                for (LocalDate l : localDateArray) {
                    result.add((E) l);
                }
                break;
            case Time:
                LocalTime[] localTimeArray = (LocalTime[]) array;
                for (LocalTime s : localTimeArray) {
                    result.add((E) s);
                }
                break;
            case InternationalPhoneNumber:
                String[] stringArray = (String[]) array;
                for (String s : stringArray) {
                    result.add((E) s);
                }
                break;
            case LocalPhoneNumber:
                stringArray = (String[]) array;
                for (String s : stringArray) {
                    result.add((E) s);
                }
                break;
            case Email:
                stringArray = (String[]) array;
                for (String s : stringArray) {
                    result.add((E) s);
                }
                break;
            case Host:
                stringArray = (String[]) array;
                for (String s : stringArray) {
                    result.add((E) s);
                }
                break;
            case QuartzCron:
                stringArray = (String[]) array;
                for (String s : stringArray) {
                    result.add((E) s);
                }
                break;
            case UnixCron:
                stringArray = (String[]) array;
                for (String s : stringArray) {
                    result.add((E) s);
                }
                break;
            case Password:
                throw new RuntimeException("Password with a multiplicity > 1 is not supported");
            case UnsecurePassword:
                stringArray = (String[]) array;
                for (String s : stringArray) {
                    result.add((E) s);
                }
                break;
            case ByteArray:
                throw new RuntimeException("Password with a multiplicity > 1 is not supported");
            default:
                throw new IllegalStateException("Unhandled dataTypeEnum " + dataTypeEnum.name());
        }
        return result;
    }

    public static <E> Object convertToArray(DataTypeEnum dataTypeEnum, Collection<E> internalCollection) {
        Collection<Object> coll = new ArrayList<>(internalCollection.size());
        coll.addAll(internalCollection.stream().map(e -> format(dataTypeEnum, e)).collect(Collectors.toList()));
        switch (dataTypeEnum) {
            case DateTime:
                LocalDateTime[] localDateTimes = coll.toArray(new LocalDateTime[coll.size()]);
                return ArrayUtils.toPrimitive(localDateTimes);
            case Date:
                LocalDate[] longDates = coll.toArray(new LocalDate[coll.size()]);
                return ArrayUtils.toPrimitive(longDates);
            case Time:
                LocalTime[] times = coll.toArray(new LocalTime[coll.size()]);
                return ArrayUtils.toPrimitive(times);
//                return coll.toArray(new String[coll.size()]);
            case InternationalPhoneNumber:
                return coll.toArray(new String[coll.size()]);
            case LocalPhoneNumber:
                return coll.toArray(new String[coll.size()]);
            case Email:
                return coll.toArray(new String[coll.size()]);
            case Host:
                return coll.toArray(new String[coll.size()]);
            case QuartzCron:
                return coll.toArray(new String[coll.size()]);
            case UnixCron:
                return coll.toArray(new String[coll.size()]);
            case Password:
                throw new RuntimeException("Password with a multiplicity > 1 is not supported");
            case UnsecurePassword:
                return coll.toArray(new String[coll.size()]);
            case ByteArray:
                throw new RuntimeException("Password with a multiplicity > 1 is not supported");
            default:
                throw new IllegalStateException("Unhandled dataTypeEnum " + dataTypeEnum.name());
        }
    }
}
