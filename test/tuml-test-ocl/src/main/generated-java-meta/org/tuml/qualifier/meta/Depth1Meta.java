package org.tuml.qualifier.meta;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.tuml.meta.BaseClassTuml;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.domain.TumlMetaNode;

public class Depth1Meta extends BaseClassTuml implements TumlMetaNode {
	static private Depth1Meta depth1Meta = null;

	/**
	 * constructor for Depth1Meta
	 * 
	 * @param vertex 
	 */
	public Depth1Meta(Vertex vertex) {
		super(vertex);
	}
	
	/**
	 * default constructor for Depth1Meta
	 */
	private Depth1Meta() {
		super(true);
		Edge edge = GraphDb.getDb().addEdge(null, GraphDb.getDb().getRoot(), this.vertex, "rootDepth1Meta");
		edge.setProperty("inClass", this.getClass().getName());
	}

	static public Depth1Meta getInstance() {
		if ( depth1Meta == null ) {
			ExecutorService es = Executors.newFixedThreadPool(1);
			es.submit(new Runnable() {
			    @Override
			    public void run() {
			        Depth1Meta.depth1Meta = new Depth1Meta();
			        GraphDb.getDb().stopTransaction(TransactionalGraph.Conclusion.SUCCESS);
			    };
			});
			es.shutdown();
			try {
				if ( !es.awaitTermination(3, TimeUnit.SECONDS) ) {
					throw new RuntimeException("Dang dog, 3 seconds to insert one miserable vertex is not enuf!!!!");
				}
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		return Depth1Meta.depth1Meta;
	}


}