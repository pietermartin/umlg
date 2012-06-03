package org.tinker.componenttest;

import com.tinkerpop.blueprints.pgm.Vertex;

import java.util.UUID;

import org.tinker.concretetest.Universe;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.collection.TumlRuntimePropertyImpl;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TinkerSetImpl;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.CompositionNode;

public class SpaceTime extends BaseTinker implements CompositionNode {
	private TinkerSet<String> name;
	private TinkerSet<Space> space;
	private TinkerSet<Time> time;
	private TinkerSet<Universe> universe;

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
		initialiseProperties();
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
		initialiseProperties();
	}

	public void addToName(String name) {
		if ( name != null ) {
			z_internalAddToName(name);
		}
	}
	
	public void addToSpace(Space space) {
		if ( space != null ) {
			space.z_internalRemoveFromSpaceTime(space.getSpaceTime());
			space.z_internalAddToSpaceTime(this);
			z_internalAddToSpace(space);
		}
	}
	
	public void addToTime(Time time) {
		if ( time != null ) {
			time.z_internalRemoveFromSpaceTime(time.getSpaceTime());
			time.z_internalAddToSpaceTime(this);
			z_internalAddToTime(time);
		}
	}
	
	public void addToUniverse(Universe universe) {
		if ( universe != null ) {
			universe.z_internalRemoveFromSpaceTime(universe.getSpaceTime());
			universe.z_internalAddToSpaceTime(this);
			z_internalAddToUniverse(universe);
		}
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
		return getUniverse();
	}
	
	public Space getSpace() {
		TinkerSet<Space> tmp = this.space;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
	}
	
	public Time getTime() {
		TinkerSet<Time> tmp = this.time;
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
	
	public Universe getUniverse() {
		TinkerSet<Universe> tmp = this.universe;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
	}
	
	@Override
	public void init(CompositionNode compositeOwner) {
		this.z_internalAddToUniverse((Universe)compositeOwner);
		this.hasInitBeenCalled = true;
		initVariables();
	}
	
	public void initVariables() {
	}
	
	@Override
	public void initialiseProperties() {
		this.name =  new TinkerSetImpl<String>(this, "org__tinker__componenttest__SpaceTime__name", true, new TumlRuntimePropertyImpl(false,false,true,false,1,1), false);
		this.space =  new TinkerSetImpl<Space>(this, "A_<spaceTime>_<space>", true, new TumlRuntimePropertyImpl(true,false,false,false,1,1), true);
		this.time =  new TinkerSetImpl<Time>(this, "A_<spaceTime>_<time>", true, new TumlRuntimePropertyImpl(true,false,false,false,1,1), true);
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
	
	public void setSpace(TinkerSet<Space> space) {
		TinkerSet<Space> oldValue = this.getSpace();
	}
	
	public void setTime(TinkerSet<Time> time) {
		TinkerSet<Time> oldValue = this.getTime();
	}
	
	public void setUniverse(TinkerSet<Universe> universe) {
		TinkerSet<Universe> oldValue = this.getUniverse();
	}
	
	public void z_internalAddToName(String name) {
		this.name.add(name);
	}
	
	public void z_internalAddToSpace(Space space) {
		this.space.add(space);
	}
	
	public void z_internalAddToTime(Time time) {
		this.time.add(time);
	}
	
	public void z_internalAddToUniverse(Universe universe) {
		this.universe.add(universe);
	}
	
	public void z_internalRemoveFromName(String name) {
		this.name.remove(name);
	}
	
	public void z_internalRemoveFromSpace(Space space) {
		this.space.remove(space);
	}
	
	public void z_internalRemoveFromTime(Time time) {
		this.time.remove(time);
	}
	
	public void z_internalRemoveFromUniverse(Universe universe) {
		this.universe.remove(universe);
	}

}