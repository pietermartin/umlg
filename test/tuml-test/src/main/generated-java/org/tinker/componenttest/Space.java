package org.tinker.componenttest;

import com.tinkerpop.blueprints.pgm.Vertex;

import java.util.UUID;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.collection.TinkerMultiplicityImpl;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TinkerSetImpl;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.CompositionNode;

public class Space extends BaseTinker implements CompositionNode {
	private TinkerSet<String> name;
	private TinkerSet<SpaceTime> spaceTime;

	/** Constructor for Space
	 * 
	 * @param compositeOwner 
	 */
	public Space(SpaceTime compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		createComponents();
		init(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/** Constructor for Space
	 * 
	 * @param vertex 
	 */
	public Space(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/** Default constructor for Space
	 */
	public Space() {
	}
	
	/** Constructor for Space
	 * 
	 * @param persistent 
	 */
	public Space(Boolean persistent) {
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
	
	public void addToSpaceTime(SpaceTime spaceTime) {
		if ( spaceTime != null ) {
			spaceTime.z_internalRemoveFromSpace(spaceTime.getSpace());
			spaceTime.z_internalAddToSpace(this);
			z_internalAddToSpaceTime(spaceTime);
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
		return getSpaceTime();
	}
	
	public SpaceTime getSpaceTime() {
		TinkerSet<SpaceTime> tmp = this.spaceTime;
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
		this.z_internalAddToSpaceTime((SpaceTime)compositeOwner);
		this.hasInitBeenCalled = true;
		initVariables();
	}
	
	public void initVariables() {
	}
	
	@Override
	public void initialiseProperties() {
		this.name =  new TinkerSetImpl<String>(this, "org__tinker__componenttest__Space__name", true, new TinkerMultiplicityImpl(false,false,true,false,1,1), false);
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
	
	public void setSpaceTime(TinkerSet<SpaceTime> spaceTime) {
		TinkerSet<SpaceTime> oldValue = this.getSpaceTime();
	}
	
	public void z_internalAddToName(String name) {
		this.name.add(name);
	}
	
	public void z_internalAddToSpaceTime(SpaceTime spaceTime) {
		this.spaceTime.add(spaceTime);
	}
	
	public void z_internalRemoveFromName(String name) {
		this.name.remove(name);
	}
	
	public void z_internalRemoveFromSpaceTime(SpaceTime spaceTime) {
		this.spaceTime.remove(spaceTime);
	}

}