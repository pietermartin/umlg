package org.tinker.concretetest;

import com.tinkerpop.blueprints.pgm.Vertex;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.CompositionNode;

public class God extends BaseTinker implements CompositionNode {


	/** Constructor for God
	 * 
	 * @param vertex 
	 */
	public God(Vertex vertex) {
		this.vertex=vertex;
	}
	
	/** Default constructor for God
	 */
	public God() {
	}
	
	/** Constructor for God
	 * 
	 * @param persistent 
	 */
	public God(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}

	@Override
	public void clearCache() {
		this.name = null;
		this.universe = null;
		this.angel = null;
		this.spirit = null;
		this.being = null;
		this.abstractSpecies = null;
		this.iMany = null;
		this.embeddedString = null;
		this.embeddedInteger = null;
		this.realRootFolder = null;
		this.fakeRootFolder = null;
		this.reason = null;
		this.pet = null;
		this.animalFarm = null;
		this.nature =  new TinkerQualifiedSetImpl<Nature>(this, "A_<god>_<nature>", getUid(), true, false, true);
		this.hand = null;
		this.foot =  new TinkerQualifiedSequenceImpl<Foot>(this, "A_<god>_<foot>", getUid(), true, false, true);
		this.world = null;
		this.fantasy =  new TinkerQualifiedOrderedSetImpl<Fantasy>(this, "A_<god>_<fantasy>", getUid(), true, false, true);
		this.many1 = null;
		this.many2 = null;
		this.dream = null;
		this.nightmare =  new TinkerQualifiedBagImpl<Nightmare>(this, "A_<god>_<nightmare>", getUid(), true, false, true);
		this.demon = null;
		this.oneOne = null;
		this.oneTwo = null;
		this.nonNavigableOne = null;
		this.nonNavigableMany = null;
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
	
	public void init() {
		this.hasInitBeenCalled = true;
		initVariables();
	}
	
	public void initVariables() {
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