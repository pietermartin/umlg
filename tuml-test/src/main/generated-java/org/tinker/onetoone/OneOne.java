package org.tinker.onetoone;

import com.tinkerpop.blueprints.pgm.Vertex;

import org.tinker.concretetest.God;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.CompositionNode;

public class OneOne extends BaseTinker implements CompositionNode {


	/** Constructor for OneOne
	 * 
	 * @param compositeOwner 
	 */
	public OneOne(God compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		createComponents();
		init(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/** Constructor for OneOne
	 * 
	 * @param vertex 
	 */
	public OneOne(Vertex vertex) {
		this.vertex=vertex;
	}
	
	/** Default constructor for OneOne
	 */
	public OneOne() {
	}
	
	/** Constructor for OneOne
	 * 
	 * @param persistent 
	 */
	public OneOne(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}

	@Override
	public void clearCache() {
		this.name = null;
	}
	
	public void createComponents() {
	}
	
	@Override
	public Long getId() {
		return TinkerIdUtilFactory.getIdUtil().getId(this.vertex);
	}
	
	@Override
	public int getObjectVersion() {
		return TinkerIdUtilFactory.getIdUtil().getVersion(this.vertex);
	}
	
	public void init(God compositeOwner) {
		this.z_internalAddToGod(owner);
		this.hasInitBeenCalled = true;
		initVariables();
	}
	
	public void initVariables() {
	}
	
	@Override
	public boolean isTinkerRoot() {
		return false;
	}
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}

}