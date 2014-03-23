package org.umlg.runtime.adaptor;


public class UMLG {

    private UMLG() {
    }

    private static ThreadLocal<UmlgGraph> dbVar = new ThreadLocal<UmlgGraph>() {
        @Override
        protected UmlgGraph initialValue() {
            return UmlgGraphManager.INSTANCE.startupGraph();
        }
    };

    public static UmlgGraph get() {
        return dbVar.get();
    }

    public static void remove() {
        dbVar.remove();
    }

    public static void incrementTransactionCount() {
        get().incrementTransactionCount();
    }

}
