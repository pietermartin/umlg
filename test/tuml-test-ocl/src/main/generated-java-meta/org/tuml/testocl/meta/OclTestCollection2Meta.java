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

public class OclTestCollection2Meta extends BaseClassTuml implements TumlMetaNode {
	static private OclTestCollection2Meta oclTestCollection2Meta = null;

	/**
	 * constructor for OclTestCollection2Meta
	 * 
	 * @param vertex 
	 */
	public OclTestCollection2Meta(Vertex vertex) {
		super(vertex);
	}
	
	/**
	 * default constructor for OclTestCollection2Meta
	 */
	private OclTestCollection2Meta() {
		super(true);
		Edge edge = GraphDb.getDb().addEdge(null, GraphDb.getDb().getRoot(), this.vertex, "rootOclTestCollection2Meta");
		edge.setProperty("inClass", this.getClass().getName());
	}

	static public OclTestCollection2Meta getInstance() {
		if ( oclTestCollection2Meta == null ) {
			ExecutorService es = Executors.newFixedThreadPool(1);
			es.submit(new Runnable() {
			    @Override
			    public void run() {
			        OclTestCollection2Meta.oclTestCollection2Meta = new OclTestCollection2Meta();
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
		return OclTestCollection2Meta.oclTestCollection2Meta;
	}


}