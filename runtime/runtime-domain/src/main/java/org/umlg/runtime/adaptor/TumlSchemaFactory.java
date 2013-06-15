package org.umlg.runtime.adaptor;

import java.lang.reflect.Method;

/**
 * Date: 2013/03/29
 * Time: 9:09 AM
 */
public class TumlSchemaFactory {

    private static TumlSchemaMap tumlSchemaUtil;

    @SuppressWarnings("unchecked")
    public static TumlSchemaMap getTumlSchemaMap() {
        if (tumlSchemaUtil == null) {
            try {
                Class<TumlSchemaMap> factory = (Class<TumlSchemaMap>) Class.forName("org.umlg.runtime.adaptor.TumlSchemaMapImpl");
                Method m = factory.getDeclaredMethod("getInstance", new Class[0]);
                tumlSchemaUtil = (TumlSchemaMap) m.invoke(null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return tumlSchemaUtil;
    }
}
