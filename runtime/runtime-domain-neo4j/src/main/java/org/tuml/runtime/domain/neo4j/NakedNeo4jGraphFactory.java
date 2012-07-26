package org.tuml.runtime.domain.neo4j;

import java.io.File;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.tuml.runtime.adaptor.NakedGraph;
import org.tuml.runtime.adaptor.NakedGraphFactory;
import org.tuml.runtime.adaptor.TinkerSchemaHelper;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;

public class NakedNeo4jGraphFactory implements NakedGraphFactory {
	
	public static NakedNeo4jGraphFactory INSTANCE = new NakedNeo4jGraphFactory();
	
	private NakedNeo4jGraphFactory() {
	}
	
	public static NakedGraphFactory getInstance() {
		return INSTANCE;
	}
	
	@Override
	public NakedGraph getNakedGraph(String url, TinkerSchemaHelper schemaHelper, boolean withSchema) {
		File f = new File(url);
		Neo4jGraph db = new Neo4jGraph(f.getAbsolutePath());
//TODO investigate
//		db.dropIndex(Index<Element>.VERTICES);
//		db.dropIndex(Index<Element>.EDGES);
//		db.setTransactionMode(Mode.MANUAL);
//		db.setMaxBufferSize(0);
		TransactionThreadEntityVar.clear();
		NakedGraph nakedGraph = new NakedNeo4jGraph(db, schemaHelper);
		nakedGraph.addRoot();
		nakedGraph.registerListeners();			
		return nakedGraph;
	}

}
