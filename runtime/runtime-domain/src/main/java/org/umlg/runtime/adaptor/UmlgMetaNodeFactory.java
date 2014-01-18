package org.umlg.runtime.adaptor;

/**
 * Date: 2013/03/23
 * Time: 10:10 PM
 */
public class UmlgMetaNodeFactory {

    private static UmlgMetaNodeManager umlgMetaNodeManager;

    @SuppressWarnings("unchecked")
    public static UmlgMetaNodeManager getUmlgMetaNodeManager() {
        if (umlgMetaNodeManager == null) {
            try {
                Class<UmlgMetaNodeManager> factory = (Class<UmlgMetaNodeManager>) Class.forName("org.umlg.model.MetaNodeCreator");
                umlgMetaNodeManager = factory.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return umlgMetaNodeManager;
    }
}
