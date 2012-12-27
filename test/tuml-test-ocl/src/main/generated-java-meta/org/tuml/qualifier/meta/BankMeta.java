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

public class BankMeta extends BaseClassTuml implements TumlMetaNode {
	static private BankMeta bankMeta = null;

	/**
	 * constructor for BankMeta
	 * 
	 * @param vertex 
	 */
	public BankMeta(Vertex vertex) {
		super(vertex);
	}
	
	/**
	 * default constructor for BankMeta
	 */
	private BankMeta() {
		super(true);
		Edge edge = GraphDb.getDb().addEdge(null, GraphDb.getDb().getRoot(), this.vertex, "rootBankMeta");
		edge.setProperty("inClass", this.getClass().getName());
	}

	static public BankMeta getInstance() {
		if ( bankMeta == null ) {
			ExecutorService es = Executors.newFixedThreadPool(1);
			es.submit(new Runnable() {
			    @Override
			    public void run() {
			        BankMeta.bankMeta = new BankMeta();
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
		return BankMeta.bankMeta;
	}


}