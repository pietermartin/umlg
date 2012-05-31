package org.tinker.concretetest;

import com.tinkerpop.blueprints.pgm.Vertex;

import org.tinker.componenttest.SpaceTime;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.CompositionNode;

public class Universe extends BaseTinker implements CompositionNode {


	/** Constructor for Universe
	 * 
	 * @param compositeOwner 
	 */
	public Universe(God compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		createComponents();
		init(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/** Constructor for Universe
	 * 
	 * @param vertex 
	 */
	public Universe(Vertex vertex) {
		this.vertex=vertex;
	}
	
	/** Default constructor for Universe
	 */
	public Universe() {
	}
	
	/** Constructor for Universe
	 * 
	 * @param persistent 
	 */
	public Universe(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}

	@Override
	public void clearCache() {
		this.name = null;
		this.spaceTime = null;
	}
	
	public void createComponents() {
		if ( getSpaceTime() == null ) {
			setSpaceTime(new SpaceTime(true));
		}
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