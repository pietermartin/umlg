package org.tinker.interfacetest;

import com.tinkerpop.blueprints.pgm.Vertex;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.CompositionNode;

public class ManyA extends BaseTinker implements CompositionNode {


	/** Constructor for ManyA
	 * 
	 * @param vertex 
	 */
	public ManyA(Vertex vertex) {
		this.vertex=vertex;
	}
	
	/** Constructor for ManyA
	 * 
	 * @param persistent 
	 */
	public ManyA(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}

	@Override
	public void clearCache() {
	}
	
	@Override
	public Long getId() {
		return TinkerIdUtilFactory.getIdUtil().getId(this.vertex);
	}
	
	@Override
	public int getObjectVersion() {
		return TinkerIdUtilFactory.getIdUtil().getVersion(this.vertex);
	}
	
	@Override
	public boolean isTinkerRoot() {
		return true;
	}
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}

}