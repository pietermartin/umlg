package org.tinker.componenttest;

import com.tinkerpop.blueprints.pgm.Vertex;

import java.util.Set;
import java.util.UUID;

import org.tinker.concretetest.Universe;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TinkerSetImpl;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.CompositionNode;
import org.tuml.runtime.domain.TinkerNode;

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
		initialiseProperties();
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
		createComponents();
	}

	public void addToName(String name) {
		if ( name != null ) {
			this.name.add(name);
		}
	}
	
	public void addToSpace(Space space) {
		if ( space != null ) {
			this.space.add(space);
		}
	}
	
	public void addToTime(Time time) {
		if ( time != null ) {
			this.time.add(time);
		}
	}
	
	public void addToUniverse(Universe universe) {
		if ( universe != null ) {
			this.universe.add(universe);
		}
	}
	
	public void clearName() {
		this.name.clear();
	}
	
	public void clearSpace() {
		this.space.clear();
	}
	
	public void clearTime() {
		this.time.clear();
	}
	
	public void clearUniverse() {
		this.universe.clear();
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
	public TinkerNode getOwningObject() {
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
	
	/** This gets called on creation with the compositional owner. The composition owner does not itself need to be a composite node
	 * 
	 * @param compositeOwner 
	 */
	@Override
	public void init(TinkerNode compositeOwner) {
		this.addToUniverse((Universe)compositeOwner);
		this.hasInitBeenCalled = true;
		initVariables();
	}
	
	public void initVariables() {
	}
	
	@Override
	public void initialiseProperties() {
		this.universe =  new TinkerSetImpl<Universe>(this, SpaceTimeRuntimePropertyEnum.universe);
		this.time =  new TinkerSetImpl<Time>(this, SpaceTimeRuntimePropertyEnum.time);
		this.space =  new TinkerSetImpl<Space>(this, SpaceTimeRuntimePropertyEnum.space);
		this.name =  new TinkerSetImpl<String>(this, SpaceTimeRuntimePropertyEnum.name);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (SpaceTimeRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case name:
				this.name =  new TinkerSetImpl<String>(this, SpaceTimeRuntimePropertyEnum.name);
			break;
		
			case space:
				this.space =  new TinkerSetImpl<Space>(this, SpaceTimeRuntimePropertyEnum.space);
			break;
		
			case time:
				this.time =  new TinkerSetImpl<Time>(this, SpaceTimeRuntimePropertyEnum.time);
			break;
		
			case universe:
				this.universe =  new TinkerSetImpl<Universe>(this, SpaceTimeRuntimePropertyEnum.universe);
			break;
		
		}
	}
	
	@Override
	public boolean isTinkerRoot() {
		return false;
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
	
	public void removeFromSpace(Set<Space> space) {
		if ( !space.isEmpty() ) {
			this.space.removeAll(space);
		}
	}
	
	public void removeFromSpace(Space space) {
		if ( space != null ) {
			this.space.remove(space);
		}
	}
	
	public void removeFromTime(Set<Time> time) {
		if ( !time.isEmpty() ) {
			this.time.removeAll(time);
		}
	}
	
	public void removeFromTime(Time time) {
		if ( time != null ) {
			this.time.remove(time);
		}
	}
	
	public void removeFromUniverse(Set<Universe> universe) {
		if ( !universe.isEmpty() ) {
			this.universe.removeAll(universe);
		}
	}
	
	public void removeFromUniverse(Universe universe) {
		if ( universe != null ) {
			this.universe.remove(universe);
		}
	}
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}
	
	public void setName(String name) {
		clearName();
		addToName(name);
	}
	
	public void setSpace(Space space) {
		clearSpace();
		addToSpace(space);
	}
	
	public void setTime(Time time) {
		clearTime();
		addToTime(time);
	}
	
	public void setUniverse(Universe universe) {
		clearUniverse();
		addToUniverse(universe);
	}

	public enum SpaceTimeRuntimePropertyEnum implements TumlRuntimeProperty {
		universe(false,false,"A_<universe>_<spaceTime>",true,false,false,false,1,1),
		time(true,true,"A_<spaceTime>_<time>",true,false,false,false,1,1),
		space(true,true,"A_<spaceTime>_<space>",true,false,false,false,1,1),
		name(true,false,"org__tinker__componenttest__SpaceTime__name",false,false,true,false,1,1);
		private boolean controllingSide;
		private boolean composite;
		private String label;
		private boolean oneToOne;
		private boolean oneToMany;
		private boolean manyToOne;
		private boolean manyToMany;
		private int upper;
		private int lower;
		/** Constructor for SpaceTimeRuntimePropertyEnum
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
		private SpaceTimeRuntimePropertyEnum(boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower) {
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
	
		static public SpaceTimeRuntimePropertyEnum fromLabel(String label) {
			if ( universe.getLabel().equals(label) ) {
				return universe;
			}
			if ( time.getLabel().equals(label) ) {
				return time;
			}
			if ( space.getLabel().equals(label) ) {
				return space;
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