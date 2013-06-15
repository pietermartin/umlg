package org.umlg.runtime.adaptor;

/**
 * Date: 2013/03/23
 * Time: 10:10 PM
 */
public class TumlMetaNodeFactory {

    private static TumlMetaNodeManager tumlMetaNodeManager;

    @SuppressWarnings("unchecked")
    public static TumlMetaNodeManager getTumlMetaNodeManager() {
        if (tumlMetaNodeManager == null) {
            try {
                Class<TumlMetaNodeManager> factory = (Class<TumlMetaNodeManager>) Class.forName("org.umlg.root.MetaNodeCreator");
                tumlMetaNodeManager = factory.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return tumlMetaNodeManager;
    }
}
