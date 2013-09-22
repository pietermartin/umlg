package org.umlg.runtime.adaptor;

import java.lang.reflect.Method;

/**
 * Date: 2013/03/29
 * Time: 9:09 AM
 */
public class UmlgSchemaFactory {

    private static UmlgSchemaMap umlgSchemaUtil;

    @SuppressWarnings("unchecked")
    public static UmlgSchemaMap getUmlgSchemaMap() {
        if (umlgSchemaUtil == null) {
            try {
                Class<UmlgSchemaMap> factory = (Class<UmlgSchemaMap>) Class.forName("org.umlg.runtime.adaptor.UmlgSchemaMapImpl");
                Method m = factory.getDeclaredMethod("getInstance", new Class[0]);
                umlgSchemaUtil = (UmlgSchemaMap) m.invoke(null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return umlgSchemaUtil;
    }
}
