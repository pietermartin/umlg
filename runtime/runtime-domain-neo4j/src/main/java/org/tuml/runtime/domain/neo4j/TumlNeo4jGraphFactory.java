package org.tuml.runtime.domain.neo4j;

import java.io.File;

import org.tuml.runtime.adaptor.TumlGraph;
import org.tuml.runtime.adaptor.TumlGraphFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;

public class TumlNeo4jGraphFactory implements TumlGraphFactory {
	
	public static TumlNeo4jGraphFactory INSTANCE = new TumlNeo4jGraphFactory();
	
	private TumlNeo4jGraphFactory() {
	}
	
	public static TumlGraphFactory getInstance() {
		return INSTANCE;
	}
	
	@Override
	public TumlGraph getTumlGraph(String url) {
		File f = new File(url);
		Neo4jGraph db = new Neo4jGraph(f.getAbsolutePath());
		TransactionThreadEntityVar.remove();
		TumlGraph nakedGraph = new TumlNeo4jGraph(db);
		nakedGraph.addRoot();
		nakedGraph.registerListeners();		
		nakedGraph.commit();
		return nakedGraph;
	}

}
