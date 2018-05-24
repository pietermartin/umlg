package org.umlg.runtime.adaptor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Date: 2014/08/18
 * Time: 2:14 PM
 */
public class SqlgDefaultValueUtil {

    public static Object valueFor(Class clazz) {
        if (clazz.equals(String.class)) {
            return "";
        } else if (clazz.equals(LocalDateTime.class)) {
            return LocalDateTime.now();
        } else if (clazz.equals(LocalDate.class)) {
            return LocalDate.now();
        } else if (clazz.equals(LocalTime.class)) {
            return LocalTime.now();
        } else if (clazz.isEnum()) {
            return "";
        } else if (clazz.equals(Integer.class)) {
            return 0;
        } else if (clazz.equals(Long.class)) {
            return 0L;
        } else if (clazz.equals(Double.class)) {
            return 0D;
        } else if (clazz.equals(Boolean.class)) {
            return true;
        } else {
            throw  new IllegalStateException("Unhandled qualifier type " + clazz.getSimpleName());
        }

    }

}
