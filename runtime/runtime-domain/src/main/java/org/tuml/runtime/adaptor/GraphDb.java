package org.tuml.runtime.adaptor;


public class GraphDb {

    private static TumlGraph staticdb;

    private GraphDb() {
    }

    private static ThreadLocal<TumlGraph> dbVar = new ThreadLocal<TumlGraph>() {
        @Override
        protected TumlGraph initialValue() {
            return staticdb;
        }
    };

    public static TumlGraph getDb() {
        return dbVar.get();
    }

    public static void setDb(TumlGraph db) {
        dbVar.remove();
        staticdb = db;
    }

    public static void remove() {
        staticdb = null;
        dbVar.remove();
    }

    public static void incrementTransactionCount() {
        getDb().incrementTransactionCount();
    }

}
