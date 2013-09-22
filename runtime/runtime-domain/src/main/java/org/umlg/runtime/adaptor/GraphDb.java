package org.umlg.runtime.adaptor;


public class GraphDb {

    private GraphDb() {
    }

    private static ThreadLocal<UmlgGraph> dbVar = new ThreadLocal<UmlgGraph>() {
        @Override
        protected UmlgGraph initialValue() {
            return UmlgGraphManager.INSTANCE.startupGraph();
        }
    };

    public static UmlgGraph getDb() {
        return dbVar.get();
    }

    public static void remove() {
        dbVar.remove();
    }

    public static void incrementTransactionCount() {
        getDb().incrementTransactionCount();
    }

}
