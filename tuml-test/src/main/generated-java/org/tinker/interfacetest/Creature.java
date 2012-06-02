package org.tinker.interfacetest;

import com.tinkerpop.blueprints.pgm.Vertex;

import java.util.UUID;

import org.tinker.concretetest.God;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.collection.TinkerMultiplicityImpl;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TinkerSetImpl;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.CompositionNode;

public class Creature extends BaseTinker implements CompositionNode {
	private TinkerSet<String> name;
	private TinkerSet<Spook> spook;

	/** Constructor for Creature
	 * 
	 * @param compositeOwner 
	 */
	public Creature(God compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		createComponents();
		init(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/** Constructor for Creature
	 * 
	 * @param vertex 
	 */
	public Creature(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/** Default constructor for Creature
	 */
	public Creature() {
	}
	
	/** Constructor for Creature
	 * 
	 * @param persistent 
	 */
	public Creature(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
		initialiseProperties();
	}

	public void addToName(String name) {
		if ( name != null ) {
			z_internalAddToName(name);
		}
	}
	
	public void addToSpook(Spook spook) {
		if ( spook != null ) {
			spook.z_internalRemoveFromCreature(spook.getCreature());
			spook.z_internalAddToCreature(this);
			z_internalAddToSpook(spook);
		}
	}
	
	public void createComponents() {
	}
	
	@Override
	public void delete() {
	}
	
	@Override
	public Long getId() {
		return TinkerIdUtilFactory.getIdUtil().getId(this.vertex);
	}
	
	public String getName() {
		TinkerSet<String> tmp = this.name;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
	}
	
	@Override
	public int getObjectVersion() {
		return TinkerIdUtilFactory.getIdUtil().getVersion(this.vertex);
	}
	
	@Override
	public CompositionNode getOwningObject() {
		return getGod();
	}
	
	public Spook getSpook() {
		TinkerSet<Spook> tmp = this.spook;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
	}
	
	@Override
	public String getUid() {
		String uid = (String) this.vertex.getProperty("uid");
		if ( uid==null || uid.trim().length()==0 ) {
			uid=UUID.randomUUID().toString();
			this.vertex.setProperty("uid", uid);
		}
		return uid;
	}
	
	@Override
	public void init(CompositionNode compositeOwner) {
		this.z_internalAddToGod((God)compositeOwner);
		this.hasInitBeenCalled = true;
		initVariables();
	}
	
	public void initVariables() {
	}
	
	@Override
	public void initialiseProperties() {
		this.name =  new TinkerSetImpl<String>(this, "org__tinker__interfacetest__Creature__name", true, new TinkerMultiplicityImpl(false,false,true,false,1,1), false);
	}
	
	@Override
	public boolean isTinkerRoot() {
		return false;
	}
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}
	
	public void setName(TinkerSet<String> name) {
	}
	
	public void setSpook(TinkerSet<Spook> spook) {
		TinkerSet<Spook> oldValue = this.getSpook();
	}
	
	public void z_internalAddToName(String name) {
		this.name.add(name);
	}
	
	public void z_internalAddToSpook(Spook spook) {
		this.spook.add(spook);
	}
	
	public void z_internalRemoveFromName(String name) {
		this.name.remove(name);
	}
	
	public void z_internalRemoveFromSpook(Spook spook) {
		this.spook.remove(spook);
	}

}