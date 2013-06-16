package org.umlg.runtime.adaptor;

import org.umlg.runtime.util.TinkerImplementation;
import org.umlg.runtime.util.UmlgProperties;

import java.lang.reflect.Method;

/**
 * Date: 2013/02/08
 * Time: 8:29 PM
 */
public class TumlExceptionUtilFactory {

    private static TumlExceptionUtil tumlExceptionUtil;

    @SuppressWarnings("unchecked")
    public static TumlExceptionUtil getTumlExceptionUtil() {
        if (tumlExceptionUtil == null) {
            try {
                TinkerImplementation tinkerImplementation = TinkerImplementation.fromName(UmlgProperties.INSTANCE.getTinkerImplementation());
                Class<TumlExceptionUtil> factory = (Class<TumlExceptionUtil>) Class.forName(tinkerImplementation.getTumlExceptionUtil());
                Method m = factory.getDeclaredMethod("getInstance", new Class[0]);
                tumlExceptionUtil = (TumlExceptionUtil) m.invoke(null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return tumlExceptionUtil;
    }
}
