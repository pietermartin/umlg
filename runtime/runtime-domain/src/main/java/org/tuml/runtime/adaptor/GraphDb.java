package org.tuml.runtime.adaptor;


public class GraphDb {

    private GraphDb() {
    }

    private static ThreadLocal<TumlGraph> dbVar = new ThreadLocal<TumlGraph>() {
        @Override
        protected TumlGraph initialValue() {
            return TumlGraphManager.INSTANCE.startupGraph();
        }
    };

    public static TumlGraph getDb() {
        return dbVar.get();
    }

    public static void remove() {
        dbVar.remove();
    }

    public static void incrementTransactionCount() {
        getDb().incrementTransactionCount();
    }

}
