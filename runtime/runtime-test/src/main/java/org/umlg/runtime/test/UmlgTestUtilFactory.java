package org.umlg.runtime.test;

import org.umlg.runtime.adaptor.UmlgAdaptorImplementation;
import org.umlg.runtime.util.UmlgUtil;

import java.lang.reflect.Method;

/**
 * Date: 2013/01/09
 * Time: 7:23 AM
 */
public class UmlgTestUtilFactory {

    private static UmlgTestUtil umlgTestUtil;

    @SuppressWarnings("unchecked")
    public static UmlgTestUtil getTestUtil() {
        if (umlgTestUtil == null) {
            try {
                UmlgAdaptorImplementation umlgAdaptorImplementation = UmlgAdaptorImplementation.fromName(UmlgUtil.getBlueprintsImplementation());
                Class factory = Class.forName(umlgAdaptorImplementation.getTumlTestUtil());
                Method m = factory.getDeclaredMethod("getInstance", new Class[0]);
                umlgTestUtil = (UmlgTestUtil) m.invoke(null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return umlgTestUtil;
    }

}
