package org.tinker.componenttest;

import com.tinkerpop.blueprints.pgm.Vertex;

import org.tinker.concretetest.Universe;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.CompositionNode;

public class SpaceTime extends BaseTinker implements CompositionNode {


	/** Constructor for SpaceTime
	 * 
	 * @param compositeOwner 
	 */
	public SpaceTime(Universe compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		createComponents();
		init(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/** Constructor for SpaceTime
	 * 
	 * @param vertex 
	 */
	public SpaceTime(Vertex vertex) {
		this.vertex=vertex;
	}
	
	/** Default constructor for SpaceTime
	 */
	public SpaceTime() {
	}
	
	/** Constructor for SpaceTime
	 * 
	 * @param persistent 
	 */
	public SpaceTime(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}

	@Override
	public void clearCache() {
		this.name = null;
		this.space = null;
		this.time = null;
	}
	
	public void createComponents() {
		if ( getSpace() == null ) {
			setSpace(new Space(true));
		}
		if ( getTime() == null ) {
			setTime(new Time(true));
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
	
	public void init(Universe compositeOwner) {
		this.z_internalAddToUniverse(owner);
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