package org.tinker.concretetest;

import com.tinkerpop.blueprints.pgm.Vertex;

import java.util.Set;
import java.util.UUID;

import org.tinker.componenttest.SpaceTime;
import org.tinker.navigability.NonNavigableMany;
import org.tinker.navigability.NonNavigableOne;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TinkerSetImpl;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.CompositionNode;
import org.tuml.runtime.domain.TinkerNode;

public class Universe extends BaseTinker implements CompositionNode {
	private TinkerSet<String> name;
	private TinkerSet<SpaceTime> spaceTime;
	private TinkerSet<God> god;
	private TinkerSet<Angel> angel;
	private TinkerSet<Demon> demon;
	private TinkerSet<NonNavigableOne> nonNavigableOne;
	private TinkerSet<NonNavigableMany> nonNavigableMany;

	/** Constructor for Universe
	 * 
	 * @param compositeOwner 
	 */
	public Universe(God compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		initialiseProperties();
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
		createComponents();
	}

	public void addToAngel(Angel angel) {
		if ( angel != null ) {
			this.angel.add(angel);
		}
	}
	
	public void addToDemon(Demon demon) {
		if ( demon != null ) {
			this.demon.add(demon);
		}
	}
	
	public void addToDemon(Set<Demon> demon) {
		if ( !demon.isEmpty() ) {
			this.demon.addAll(demon);
		}
	}
	
	public void addToGod(God god) {
		if ( god != null ) {
			this.god.add(god);
		}
	}
	
	public void addToName(String name) {
		if ( name != null ) {
			this.name.add(name);
		}
	}
	
	public void addToNonNavigableMany(NonNavigableMany nonNavigableMany) {
		if ( nonNavigableMany != null ) {
			this.nonNavigableMany.add(nonNavigableMany);
		}
	}
	
	public void addToNonNavigableMany(Set<NonNavigableMany> nonNavigableMany) {
		if ( !nonNavigableMany.isEmpty() ) {
			this.nonNavigableMany.addAll(nonNavigableMany);
		}
	}
	
	public void addToNonNavigableOne(NonNavigableOne nonNavigableOne) {
		if ( nonNavigableOne != null ) {
			this.nonNavigableOne.add(nonNavigableOne);
		}
	}
	
	public void addToSpaceTime(SpaceTime spaceTime) {
		if ( spaceTime != null ) {
			this.spaceTime.add(spaceTime);
		}
	}
	
	public void clearAngel() {
		this.angel.clear();
	}
	
	public void clearDemon() {
		this.demon.clear();
	}
	
	public void clearGod() {
		this.god.clear();
	}
	
	public void clearName() {
		this.name.clear();
	}
	
	public void clearNonNavigableMany() {
		this.nonNavigableMany.clear();
	}
	
	public void clearNonNavigableOne() {
		this.nonNavigableOne.clear();
	}
	
	public void clearSpaceTime() {
		this.spaceTime.clear();
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
	public TinkerNode getOwningObject() {
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
	
	/** This gets called on creation with the compositional owner. The composition owner does not itself need to be a composite node
	 * 
	 * @param compositeOwner 
	 */
	@Override
	public void init(TinkerNode compositeOwner) {
		this.addToGod((God)compositeOwner);
		this.hasInitBeenCalled = true;
		initVariables();
	}
	
	public void initVariables() {
	}
	
	@Override
	public void initialiseProperties() {
		this.demon =  new TinkerSetImpl<Demon>(this, UniverseRuntimePropertyEnum.demon);
		this.nonNavigableOne =  new TinkerSetImpl<NonNavigableOne>(this, UniverseRuntimePropertyEnum.nonNavigableOne);
		this.god =  new TinkerSetImpl<God>(this, UniverseRuntimePropertyEnum.god);
		this.angel =  new TinkerSetImpl<Angel>(this, UniverseRuntimePropertyEnum.angel);
		this.nonNavigableMany =  new TinkerSetImpl<NonNavigableMany>(this, UniverseRuntimePropertyEnum.nonNavigableMany);
		this.spaceTime =  new TinkerSetImpl<SpaceTime>(this, UniverseRuntimePropertyEnum.spaceTime);
		this.name =  new TinkerSetImpl<String>(this, UniverseRuntimePropertyEnum.name);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (UniverseRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case name:
				this.name =  new TinkerSetImpl<String>(this, UniverseRuntimePropertyEnum.name);
			break;
		
			case spaceTime:
				this.spaceTime =  new TinkerSetImpl<SpaceTime>(this, UniverseRuntimePropertyEnum.spaceTime);
			break;
		
			case nonNavigableMany:
				this.nonNavigableMany =  new TinkerSetImpl<NonNavigableMany>(this, UniverseRuntimePropertyEnum.nonNavigableMany);
			break;
		
			case angel:
				this.angel =  new TinkerSetImpl<Angel>(this, UniverseRuntimePropertyEnum.angel);
			break;
		
			case god:
				this.god =  new TinkerSetImpl<God>(this, UniverseRuntimePropertyEnum.god);
			break;
		
			case nonNavigableOne:
				this.nonNavigableOne =  new TinkerSetImpl<NonNavigableOne>(this, UniverseRuntimePropertyEnum.nonNavigableOne);
			break;
		
			case demon:
				this.demon =  new TinkerSetImpl<Demon>(this, UniverseRuntimePropertyEnum.demon);
			break;
		
		}
	}
	
	@Override
	public boolean isTinkerRoot() {
		return false;
	}
	
	public void removeFromAngel(Angel angel) {
		if ( angel != null ) {
			this.angel.remove(angel);
		}
	}
	
	public void removeFromAngel(Set<Angel> angel) {
		if ( !angel.isEmpty() ) {
			this.angel.removeAll(angel);
		}
	}
	
	public void removeFromDemon(Demon demon) {
		if ( demon != null ) {
			this.demon.remove(demon);
		}
	}
	
	public void removeFromDemon(Set<Demon> demon) {
		if ( !demon.isEmpty() ) {
			this.demon.removeAll(demon);
		}
	}
	
	public void removeFromGod(God god) {
		if ( god != null ) {
			this.god.remove(god);
		}
	}
	
	public void removeFromGod(Set<God> god) {
		if ( !god.isEmpty() ) {
			this.god.removeAll(god);
		}
	}
	
	public void removeFromName(Set<String> name) {
		if ( !name.isEmpty() ) {
			this.name.removeAll(name);
		}
	}
	
	public void removeFromName(String name) {
		if ( name != null ) {
			this.name.remove(name);
		}
	}
	
	public void removeFromNonNavigableMany(NonNavigableMany nonNavigableMany) {
		if ( nonNavigableMany != null ) {
			this.nonNavigableMany.remove(nonNavigableMany);
		}
	}
	
	public void removeFromNonNavigableMany(Set<NonNavigableMany> nonNavigableMany) {
		if ( !nonNavigableMany.isEmpty() ) {
			this.nonNavigableMany.removeAll(nonNavigableMany);
		}
	}
	
	public void removeFromNonNavigableOne(NonNavigableOne nonNavigableOne) {
		if ( nonNavigableOne != null ) {
			this.nonNavigableOne.remove(nonNavigableOne);
		}
	}
	
	public void removeFromNonNavigableOne(Set<NonNavigableOne> nonNavigableOne) {
		if ( !nonNavigableOne.isEmpty() ) {
			this.nonNavigableOne.removeAll(nonNavigableOne);
		}
	}
	
	public void removeFromSpaceTime(Set<SpaceTime> spaceTime) {
		if ( !spaceTime.isEmpty() ) {
			this.spaceTime.removeAll(spaceTime);
		}
	}
	
	public void removeFromSpaceTime(SpaceTime spaceTime) {
		if ( spaceTime != null ) {
			this.spaceTime.remove(spaceTime);
		}
	}
	
	public void setAngel(Angel angel) {
		clearAngel();
		addToAngel(angel);
	}
	
	public void setDemon(Set<Demon> demon) {
		clearDemon();
		addToDemon(demon);
	}
	
	public void setGod(God god) {
		clearGod();
		addToGod(god);
	}
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}
	
	public void setName(String name) {
		clearName();
		addToName(name);
	}
	
	public void setNonNavigableMany(Set<NonNavigableMany> nonNavigableMany) {
		clearNonNavigableMany();
		addToNonNavigableMany(nonNavigableMany);
	}
	
	public void setNonNavigableOne(NonNavigableOne nonNavigableOne) {
		clearNonNavigableOne();
		addToNonNavigableOne(nonNavigableOne);
	}
	
	public void setSpaceTime(SpaceTime spaceTime) {
		clearSpaceTime();
		addToSpaceTime(spaceTime);
	}

	public enum UniverseRuntimePropertyEnum implements TumlRuntimeProperty {
		demon(true,false,"A_<universe>_<demon>",false,true,false,false,-1,1),
		nonNavigableOne(false,false,"A_<universe>_<nonNavigableOne>",true,false,false,false,1,0),
		god(false,false,"A_<god>_<universe>",false,false,true,false,1,1),
		angel(false,false,"A_<universe>_<angel>",true,false,false,false,1,0),
		nonNavigableMany(true,false,"A_<universe>_<nonNavigableMany>",false,true,false,false,-1,0),
		spaceTime(true,true,"A_<universe>_<spaceTime>",true,false,false,false,1,1),
		name(true,false,"org__tinker__concretetest__Universe__name",false,false,true,false,1,1);
		private boolean controllingSide;
		private boolean composite;
		private String label;
		private boolean oneToOne;
		private boolean oneToMany;
		private boolean manyToOne;
		private boolean manyToMany;
		private int upper;
		private int lower;
		/** Constructor for UniverseRuntimePropertyEnum
		 * 
		 * @param controllingSide 
		 * @param composite 
		 * @param label 
		 * @param oneToOne 
		 * @param oneToMany 
		 * @param manyToOne 
		 * @param manyToMany 
		 * @param upper 
		 * @param lower 
		 */
		private UniverseRuntimePropertyEnum(boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower) {
			this.controllingSide = controllingSide;
			this.composite = composite;
			this.label = label;
			this.oneToOne = oneToOne;
			this.oneToMany = oneToMany;
			this.manyToOne = manyToOne;
			this.manyToMany = manyToMany;
			this.upper = upper;
			this.lower = lower;
		}
	
		static public UniverseRuntimePropertyEnum fromLabel(String label) {
			if ( demon.getLabel().equals(label) ) {
				return demon;
			}
			if ( nonNavigableOne.getLabel().equals(label) ) {
				return nonNavigableOne;
			}
			if ( god.getLabel().equals(label) ) {
				return god;
			}
			if ( angel.getLabel().equals(label) ) {
				return angel;
			}
			if ( nonNavigableMany.getLabel().equals(label) ) {
				return nonNavigableMany;
			}
			if ( spaceTime.getLabel().equals(label) ) {
				return spaceTime;
			}
			if ( name.getLabel().equals(label) ) {
				return name;
			}
			throw new IllegalStateException();
		}
		
		public String getLabel() {
			return this.label;
		}
		
		public int getLower() {
			return this.lower;
		}
		
		public int getUpper() {
			return this.upper;
		}
		
		public boolean isComposite() {
			return this.composite;
		}
		
		public boolean isControllingSide() {
			return this.controllingSide;
		}
		
		public boolean isManyToMany() {
			return this.manyToMany;
		}
		
		public boolean isManyToOne() {
			return this.manyToOne;
		}
		
		public boolean isOneToMany() {
			return this.oneToMany;
		}
		
		public boolean isOneToOne() {
			return this.oneToOne;
		}
		
		@Override
		public boolean isValid(int elementCount) {
			return (getUpper() == -1 || elementCount <= getUpper()) && elementCount >= getLower();
		}
	
	}
}