package org.umlg.runtime.adaptor;

import java.lang.reflect.Method;

/**
 * Date: 2013/03/29
 * Time: 9:09 AM
 */
public class UmlgSchemaCreatorFactory {

    private static UmlgSchemaCreator umlgSchemaCreator;

    @SuppressWarnings("unchecked")
    public static UmlgSchemaCreator getUmlgSchemaCreator() {
        if (umlgSchemaCreator == null) {
            try {
                Class<UmlgSchemaMap> factory = (Class<UmlgSchemaMap>) Class.forName("org.umlg.runtime.adaptor.UmlgSchemaCreatorImpl");
                Method m = factory.getDeclaredMethod("getInstance", new Class[0]);
                umlgSchemaCreator = (UmlgSchemaCreator) m.invoke(null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return umlgSchemaCreator;
    }
}
