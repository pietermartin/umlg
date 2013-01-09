package org.tuml.runtime.test;

import org.tuml.runtime.adaptor.TinkerIdUtil;
import org.tuml.runtime.util.TinkerImplementation;
import org.tuml.runtime.util.TumlProperties;

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
                TinkerImplementation tinkerImplementation = TinkerImplementation.fromName(TumlProperties.INSTANCE.getTinkerImplementation());
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
