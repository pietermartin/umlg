package org.umlg.runtime.adaptor;

import org.umlg.runtime.util.UmlgAdaptorImplementation;
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
                UmlgAdaptorImplementation umlgAdaptorImplementation = UmlgAdaptorImplementation.fromName(UmlgProperties.INSTANCE.getTinkerImplementation());
                Class<TumlExceptionUtil> factory = (Class<TumlExceptionUtil>) Class.forName(umlgAdaptorImplementation.getTumlExceptionUtil());
                Method m = factory.getDeclaredMethod("getInstance", new Class[0]);
                tumlExceptionUtil = (TumlExceptionUtil) m.invoke(null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return tumlExceptionUtil;
    }
}
