package org.tinker.componenttest;

import com.tinkerpop.blueprints.Vertex;

import java.util.Set;
import java.util.UUID;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TinkerSetImpl;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.CompositionNode;
import org.tuml.runtime.domain.TinkerNode;

public class Time extends BaseTinker implements CompositionNode {
	private TinkerSet<String> name;
	private TinkerSet<SpaceTime> spaceTime;

	/** Constructor for Time
	 * 
	 * @param compositeOwner 
	 */
	public Time(SpaceTime compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		initialiseProperties();
		createComponents();
		init(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/** Constructor for Time
	 * 
	 * @param vertex 
	 */
	public Time(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/** Default constructor for Time
	 */
	public Time() {
	}
	
	/** Constructor for Time
	 * 
	 * @param persistent 
	 */
	public Time(Boolean persistent) {
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
	
	public void addToSpaceTime(SpaceTime spaceTime) {
		if ( spaceTime != null ) {
			this.spaceTime.add(spaceTime);
		}
	}
	
	public void clearName() {
		this.name.clear();
	}
	
	public void clearSpaceTime() {
		this.spaceTime.clear();
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
	public TinkerNode getOwningObject() {
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
	
	/** This gets called on creation with the compositional owner. The composition owner does not itself need to be a composite node
	 * 
	 * @param compositeOwner 
	 */
	@Override
	public void init(TinkerNode compositeOwner) {
		this.addToSpaceTime((SpaceTime)compositeOwner);
		this.hasInitBeenCalled = true;
		initVariables();
	}
	
	public void initVariables() {
	}
	
	@Override
	public void initialiseProperties() {
		this.name =  new TinkerSetImpl<String>(this, TimeRuntimePropertyEnum.name);
		this.spaceTime =  new TinkerSetImpl<SpaceTime>(this, TimeRuntimePropertyEnum.spaceTime);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (TimeRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case spaceTime:
				this.spaceTime =  new TinkerSetImpl<SpaceTime>(this, TimeRuntimePropertyEnum.spaceTime);
			break;
		
			case name:
				this.name =  new TinkerSetImpl<String>(this, TimeRuntimePropertyEnum.name);
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
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}
	
	public void setName(String name) {
		clearName();
		addToName(name);
	}
	
	public void setSpaceTime(SpaceTime spaceTime) {
		clearSpaceTime();
		addToSpaceTime(spaceTime);
	}

	public enum TimeRuntimePropertyEnum implements TumlRuntimeProperty {
		name(true,true,false,"org__tinker__componenttest__Time__name",false,false,true,false,1,1),
		spaceTime(false,false,false,"A_<spaceTime>_<time>",true,false,false,false,1,1);
		private boolean onePrimitive;
		private boolean controllingSide;
		private boolean composite;
		private String label;
		private boolean oneToOne;
		private boolean oneToMany;
		private boolean manyToOne;
		private boolean manyToMany;
		private int upper;
		private int lower;
		/** Constructor for TimeRuntimePropertyEnum
		 * 
		 * @param onePrimitive 
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
		private TimeRuntimePropertyEnum(boolean onePrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower) {
			this.onePrimitive = onePrimitive;
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
	
		static public TimeRuntimePropertyEnum fromLabel(String label) {
			if ( name.getLabel().equals(label) ) {
				return name;
			}
			if ( spaceTime.getLabel().equals(label) ) {
				return spaceTime;
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
		
		public boolean isOnePrimitive() {
			return this.onePrimitive;
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