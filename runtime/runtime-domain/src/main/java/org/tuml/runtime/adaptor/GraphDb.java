package org.tuml.runtime.adaptor;


import com.tinkerpop.blueprints.Vertex;

import java.util.HashMap;
import java.util.Map;

public class GraphDb {

    private static NakedGraph staticdb;

    private GraphDb() {
    }

    private static ThreadLocal<NakedGraph> dbVar = new ThreadLocal<NakedGraph>() {
        @Override
        public void set(NakedGraph db) {
            super.set(db);
        }

        @Override
        protected NakedGraph initialValue() {
            return staticdb;
        }
    };

    public static NakedGraph getDb() {
        return dbVar.get();
//		NakedGraph nakedGraph = dbVar.get();
//		if (nakedGraph==null) {
//			nakedGraph = staticdb;
//			setDb(nakedGraph);
//		}
//		return nakedGraph;
    }

    public static void setDb(NakedGraph db) {
        staticdb = db;
        dbVar.set(db);
        if (db == null) {
            dbVar.remove();
        }
    }

    public static void remove() {
        dbVar.remove();
    }

    public static void incrementTransactionCount() {
        getDb().incrementTransactionCount();
    }

}
