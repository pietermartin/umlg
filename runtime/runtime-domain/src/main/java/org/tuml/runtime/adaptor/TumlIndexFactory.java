package org.tuml.runtime.adaptor;

/**
 * Date: 2013/03/23
 * Time: 10:10 PM
 */
public class TumlIndexFactory {

    private static TumlIndexManager tumlIndexManager;

    @SuppressWarnings("unchecked")
    public static TumlIndexManager getTumlIndexManager() {
        if (tumlIndexManager == null) {
            try {
                Class<TumlIndexManager> factory = (Class<TumlIndexManager>) Class.forName("org.tuml.root.IndexCreator");
                tumlIndexManager = factory.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return tumlIndexManager;
    }
}
