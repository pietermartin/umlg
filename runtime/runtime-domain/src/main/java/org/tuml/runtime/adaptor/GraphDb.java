package org.tuml.runtime.adaptor;


public class GraphDb {

    private static TumlGraph staticdb;

    private GraphDb() {
    }

    private static ThreadLocal<TumlGraph> dbVar = new ThreadLocal<TumlGraph>() {
//        @Override
//        public void set(TumlGraph db) {
//            super.set(db);
//        }
        @Override
        protected TumlGraph initialValue() {
            return staticdb;
        }
    };

    public static TumlGraph getDb() {
        return dbVar.get();
//		TumlGraph nakedGraph = dbVar.get();
//		if (nakedGraph==null) {
//			nakedGraph = staticdb;
//			setDb(nakedGraph);
//		}
//		return nakedGraph;
    }

    public static void setDb(TumlGraph db) {
        staticdb = db;
//        dbVar.set(db);
//        if (db == null) {
//            dbVar.remove();
//        }
    }

    public static void remove() {
        dbVar.remove();
    }

    public static void incrementTransactionCount() {
        getDb().incrementTransactionCount();
    }

}
