package org.tuml.runtime.adaptor;



public class GraphDb {

	private static NakedGraph staticdb;
	
	private GraphDb() {
	}

	private static ThreadLocal<NakedGraph> dbVar = new ThreadLocal<NakedGraph>() {
		@Override
		public NakedGraph get() {
			return super.get();
		}
		@Override
		public void set(NakedGraph db) {
			super.set(db);
		}
	};

	public static NakedGraph getDb() {
		NakedGraph nakedGraph = dbVar.get();
		if (nakedGraph==null) {
			nakedGraph = staticdb;
//			nakedGraph.setMaxBufferSize(0);
			setDb(nakedGraph);
		}
		return nakedGraph;
	}
	
	public static void setDb(NakedGraph db) {
		staticdb = db;
		dbVar.set(db);
		if (db==null) {
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
