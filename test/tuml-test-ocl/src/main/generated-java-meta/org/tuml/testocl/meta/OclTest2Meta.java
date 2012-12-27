package org.tuml.testocl.meta;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.tuml.meta.BaseClassTuml;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.domain.TumlMetaNode;

public class OclTest2Meta extends BaseClassTuml implements TumlMetaNode {
	static private OclTest2Meta oclTest2Meta = null;

	/**
	 * constructor for OclTest2Meta
	 * 
	 * @param vertex 
	 */
	public OclTest2Meta(Vertex vertex) {
		super(vertex);
	}
	
	/**
	 * default constructor for OclTest2Meta
	 */
	private OclTest2Meta() {
		super(true);
		Edge edge = GraphDb.getDb().addEdge(null, GraphDb.getDb().getRoot(), this.vertex, "rootOclTest2Meta");
		edge.setProperty("inClass", this.getClass().getName());
	}

	static public OclTest2Meta getInstance() {
		if ( oclTest2Meta == null ) {
			ExecutorService es = Executors.newFixedThreadPool(1);
			es.submit(new Runnable() {
			    @Override
			    public void run() {
			        OclTest2Meta.oclTest2Meta = new OclTest2Meta();
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
		return OclTest2Meta.oclTest2Meta;
	}


}