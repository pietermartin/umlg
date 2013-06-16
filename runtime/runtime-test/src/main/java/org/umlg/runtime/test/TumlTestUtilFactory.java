package org.umlg.runtime.test;

import org.umlg.runtime.adaptor.TinkerIdUtil;
import org.umlg.runtime.util.TinkerImplementation;
import org.umlg.runtime.util.UmlgProperties;

import java.lang.reflect.Method;

/**
 * Date: 2013/01/09
 * Time: 7:23 AM
 */
public class TumlTestUtilFactory {

    private static TumlTestUtil tumlTestUtil;

    @SuppressWarnings("unchecked")
    public static TumlTestUtil getTestUtil() {
        if (tumlTestUtil == null) {
            try {
                TinkerImplementation tinkerImplementation = TinkerImplementation.fromName(UmlgProperties.INSTANCE.getTinkerImplementation());
                Class<TinkerIdUtil> factory = (Class<TinkerIdUtil>) Class.forName(tinkerImplementation.getTumlTestUtil());
                Method m = factory.getDeclaredMethod("getInstance", new Class[0]);
                tumlTestUtil = (TumlTestUtil) m.invoke(null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return tumlTestUtil;
    }

}
