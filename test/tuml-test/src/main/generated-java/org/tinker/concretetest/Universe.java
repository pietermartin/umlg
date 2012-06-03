package org.tinker.concretetest;

import com.tinkerpop.blueprints.pgm.Vertex;

import java.util.UUID;

import org.tinker.componenttest.SpaceTime;
import org.tinker.navigability.NonNavigableMany;
import org.tinker.navigability.NonNavigableOne;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.collection.TumlRuntimePropertyImpl;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TinkerSetImpl;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.CompositionNode;

public class Universe extends BaseTinker implements CompositionNode {
	private TinkerSet<Angel> angel;
	private TinkerSet<Demon> demon;
	private TinkerSet<God> god;
	private TinkerSet<String> name;
	private TinkerSet<NonNavigableMany> nonNavigableMany;
	private TinkerSet<NonNavigableOne> nonNavigableOne;
	private TinkerSet<SpaceTime> spaceTime;

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
		initialiseProperties();
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
		initialiseProperties();
	}

	public void addToAngel(Angel angel) {
		if ( angel != null ) {
			angel.z_internalRemoveFromUniverse(angel.getUniverse());
			angel.z_internalAddToUniverse(this);
			z_internalAddToAngel(angel);
		}
	}
	
	public void addToDemon(Demon demon) {
		if ( demon != null ) {
			demon.z_internalRemoveFromUniverse(demon.getUniverse());
			demon.z_internalAddToUniverse(this);
			z_internalAddToDemon(demon);
		}
	}
	
	public void addToGod(God god) {
		if ( god != null ) {
			god.z_internalRemoveFromUniverse(god.getUniverse());
			god.z_internalAddToUniverse(this);
			z_internalAddToGod(god);
		}
	}
	
	public void addToName(String name) {
		if ( name != null ) {
			z_internalAddToName(name);
		}
	}
	
	public void addToNonNavigableMany(NonNavigableMany nonNavigableMany) {
		if ( nonNavigableMany != null ) {
			nonNavigableMany.z_internalRemoveFromUniverse(nonNavigableMany.getUniverse());
			nonNavigableMany.z_internalAddToUniverse(this);
			z_internalAddToNonNavigableMany(nonNavigableMany);
		}
	}
	
	public void addToNonNavigableOne(NonNavigableOne nonNavigableOne) {
		if ( nonNavigableOne != null ) {
			nonNavigableOne.z_internalRemoveFromUniverse(nonNavigableOne.getUniverse());
			nonNavigableOne.z_internalAddToUniverse(this);
			z_internalAddToNonNavigableOne(nonNavigableOne);
		}
	}
	
	public void addToSpaceTime(SpaceTime spaceTime) {
		if ( spaceTime != null ) {
			spaceTime.z_internalRemoveFromUniverse(spaceTime.getUniverse());
			spaceTime.z_internalAddToUniverse(this);
			z_internalAddToSpaceTime(spaceTime);
		}
	}
	
	public void createComponents() {
		if ( getSpaceTime() == null ) {
			setSpaceTime(new SpaceTime(true));
		}
	}
	
	@Override
	public void delete() {
	}
	
	public Angel getAngel() {
		TinkerSet<Angel> tmp = this.angel;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
	}
	
	public TinkerSet<Demon> getDemon() {
		return this.demon;
	}
	
	public God getGod() {
		TinkerSet<God> tmp = this.god;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
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
	
	public TinkerSet<NonNavigableMany> getNonNavigableMany() {
		return this.nonNavigableMany;
	}
	
	public NonNavigableOne getNonNavigableOne() {
		TinkerSet<NonNavigableOne> tmp = this.nonNavigableOne;
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
		this.z_internalAddToGod((God)compositeOwner);
		this.hasInitBeenCalled = true;
		initVariables();
	}
	
	public void initVariables() {
	}
	
	@Override
	public void initialiseProperties() {
		this.name =  new TinkerSetImpl<String>(this, "org__tinker__concretetest__Universe__name", true, new TumlRuntimePropertyImpl(false,false,true,false,1,1), false);
		this.spaceTime =  new TinkerSetImpl<SpaceTime>(this, "A_<universe>_<spaceTime>", true, new TumlRuntimePropertyImpl(true,false,false,false,1,1), true);
	}
	
	@Override
	public boolean isTinkerRoot() {
		return false;
	}
	
	public void setAngel(TinkerSet<Angel> angel) {
		TinkerSet<Angel> oldValue = this.getAngel();
	}
	
	public void setGod(TinkerSet<God> god) {
	}
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}
	
	public void setName(TinkerSet<String> name) {
	}
	
	public void setNonNavigableOne(TinkerSet<NonNavigableOne> nonNavigableOne) {
	}
	
	public void setSpaceTime(TinkerSet<SpaceTime> spaceTime) {
		TinkerSet<SpaceTime> oldValue = this.getSpaceTime();
	}
	
	public void z_internalAddToAngel(Angel angel) {
		this.angel.add(angel);
	}
	
	public void z_internalAddToDemon(Demon demon) {
		this.demon.add(demon);
	}
	
	public void z_internalAddToGod(God god) {
		this.god.add(god);
	}
	
	public void z_internalAddToName(String name) {
		this.name.add(name);
	}
	
	public void z_internalAddToNonNavigableMany(NonNavigableMany nonNavigableMany) {
		this.nonNavigableMany.add(nonNavigableMany);
	}
	
	public void z_internalAddToNonNavigableOne(NonNavigableOne nonNavigableOne) {
		this.nonNavigableOne.add(nonNavigableOne);
	}
	
	public void z_internalAddToSpaceTime(SpaceTime spaceTime) {
		this.spaceTime.add(spaceTime);
	}
	
	public void z_internalRemoveFromAngel(Angel angel) {
		this.angel.remove(angel);
	}
	
	public void z_internalRemoveFromDemon(Demon demon) {
		this.demon.remove(demon);
	}
	
	public void z_internalRemoveFromGod(God god) {
		this.god.remove(god);
	}
	
	public void z_internalRemoveFromName(String name) {
		this.name.remove(name);
	}
	
	public void z_internalRemoveFromNonNavigableMany(NonNavigableMany nonNavigableMany) {
		this.nonNavigableMany.remove(nonNavigableMany);
	}
	
	public void z_internalRemoveFromNonNavigableOne(NonNavigableOne nonNavigableOne) {
		this.nonNavigableOne.remove(nonNavigableOne);
	}
	
	public void z_internalRemoveFromSpaceTime(SpaceTime spaceTime) {
		this.spaceTime.remove(spaceTime);
	}

}