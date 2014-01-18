package org.umlg.runtime.adaptor;

/**
 * Date: 2013/03/23
 * Time: 10:10 PM
 */
public class UmlGIndexFactory {

    private static UmlgIndexManager umlgIndexManager;

    @SuppressWarnings("unchecked")
    public static UmlgIndexManager getUmlgIndexManager() {
        if (umlgIndexManager == null) {
            try {
                Class<UmlgIndexManager> factory = (Class<UmlgIndexManager>) Class.forName("org.umlg.model.IndexCreator");
                umlgIndexManager = factory.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return umlgIndexManager;
    }
}
