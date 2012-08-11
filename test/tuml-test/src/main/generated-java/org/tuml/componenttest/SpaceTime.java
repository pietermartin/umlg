package org.tuml.componenttest;

import com.tinkerpop.blueprints.Vertex;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.codehaus.jackson.map.ObjectMapper;
import org.tuml.componenttest.Space.SpaceRuntimePropertyEnum;
import org.tuml.componenttest.Time.TimeRuntimePropertyEnum;
import org.tuml.concretetest.Universe;
import org.tuml.concretetest.Universe.UniverseRuntimePropertyEnum;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.collection.Qualifier;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.collection.persistent.TinkerSetImpl;
import org.tuml.runtime.domain.BaseTuml;
import org.tuml.runtime.domain.CompositionNode;
import org.tuml.runtime.domain.TumlNode;

public class SpaceTime extends BaseTuml implements CompositionNode {
	static final public long serialVersionUID = 1L;
	private TinkerSet<String> name;
	private TinkerSet<Space> space;
	private TinkerSet<Time> time;
	private TinkerSet<Universe> universe;

	/**
	 * constructor for SpaceTime
	 * 
	 * @param compositeOwner 
	 */
	public SpaceTime(Universe compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		initialiseProperties();
		initVariables();
		createComponents();
		addToUniverse(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/**
	 * constructor for SpaceTime
	 * 
	 * @param vertex 
	 */
	public SpaceTime(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/**
	 * default constructor for SpaceTime
	 */
	public SpaceTime() {
	}
	
	/**
	 * constructor for SpaceTime
	 * 
	 * @param persistent 
	 */
	public SpaceTime(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		this.vertex.setProperty("className", getClass().getName());
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
		initialiseProperties();
		initVariables();
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
		if ( getSpace() != null ) {
			getSpace().delete();
		}
		if ( getTime() != null ) {
			getTime().delete();
		}
		this.universe.clear();
		GraphDb.getDb().removeVertex(this.vertex);
	}
	
	@Override
	public void fromJson(Map<String,Object> propertyMap) {
		for ( String propertyName : propertyMap.keySet() ) {
			if ( propertyName.equals("space") ) {
				setSpace((org.tuml.componenttest.Space)propertyMap.get(propertyName));
			} else if ( propertyName.equals("time") ) {
				setTime((org.tuml.componenttest.Time)propertyMap.get(propertyName));
			} else if ( propertyName.equals("name") ) {
				setName((String)propertyMap.get(propertyName));
			} else if ( propertyName.equals("id") ) {
				//Ignored;
			} else {
				throw new IllegalStateException();
			}
		}
	}
	
	@Override
	public void fromJson(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			@SuppressWarnings(	"unchecked")
			 Map<String,Object> propertyMap = mapper.readValue(json, Map.class);
			fromJson(propertyMap);
		} catch (Exception e) {
			throw new RuntimeException(e);
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
	
	@Override
	public int getObjectVersion() {
		return TinkerIdUtilFactory.getIdUtil().getVersion(this.vertex);
	}
	
	@Override
	public TumlNode getOwningObject() {
		return getUniverse();
	}
	
	/**
	 * getQualifiers is called from the collection in order to update the index used to implement the qualifier
	 * 
	 * @param tumlRuntimeProperty 
	 * @param node 
	 */
	@Override
	public List<Qualifier> getQualifiers(TumlRuntimeProperty tumlRuntimeProperty, TumlNode node) {
		List<Qualifier> result = Collections.emptyList();
		SpaceTimeRuntimePropertyEnum runtimeProperty = SpaceTimeRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result.isEmpty() ) {
			switch ( runtimeProperty ) {
				default:
					result = Collections.emptyList();
				break;
			}
		
		}
		return result;
	}
	
	/**
	 * getSize is called from the collection in order to update the index used to implement a sequence's index
	 * 
	 * @param tumlRuntimeProperty 
	 */
	@Override
	public int getSize(TumlRuntimeProperty tumlRuntimeProperty) {
		int result = 0;
		SpaceTimeRuntimePropertyEnum runtimeProperty = SpaceTimeRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result == 0 ) {
			switch ( runtimeProperty ) {
				case name:
					result = name.size();
				break;
			
				case universe:
					result = universe.size();
				break;
			
				case time:
					result = time.size();
				break;
			
				case space:
					result = space.size();
				break;
			
				default:
					result = 0;
				break;
			}
		
		}
		return result;
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
	
	public void initVariables() {
	}
	
	@Override
	public void initialiseProperties() {
		this.space =  new TinkerSetImpl<Space>(this, SpaceTimeRuntimePropertyEnum.space);
		this.time =  new TinkerSetImpl<Time>(this, SpaceTimeRuntimePropertyEnum.time);
		this.universe =  new TinkerSetImpl<Universe>(this, SpaceTimeRuntimePropertyEnum.universe);
		this.name =  new TinkerSetImpl<String>(this, SpaceTimeRuntimePropertyEnum.name);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (SpaceTimeRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case name:
				this.name =  new TinkerSetImpl<String>(this, SpaceTimeRuntimePropertyEnum.name);
			break;
		
			case universe:
				this.universe =  new TinkerSetImpl<Universe>(this, SpaceTimeRuntimePropertyEnum.universe);
			break;
		
			case time:
				this.time =  new TinkerSetImpl<Time>(this, SpaceTimeRuntimePropertyEnum.time);
			break;
		
			case space:
				this.space =  new TinkerSetImpl<Space>(this, SpaceTimeRuntimePropertyEnum.space);
			break;
		
		}
	}
	
	@Override
	public boolean isTinkerRoot() {
		return false;
	}
	
	public void removeFromName(String name) {
		if ( name != null ) {
			this.name.remove(name);
		}
	}
	
	public void removeFromName(TinkerSet<String> name) {
		if ( !name.isEmpty() ) {
			this.name.removeAll(name);
		}
	}
	
	public void removeFromSpace(Space space) {
		if ( space != null ) {
			this.space.remove(space);
		}
	}
	
	public void removeFromSpace(TinkerSet<Space> space) {
		if ( !space.isEmpty() ) {
			this.space.removeAll(space);
		}
	}
	
	public void removeFromTime(Time time) {
		if ( time != null ) {
			this.time.remove(time);
		}
	}
	
	public void removeFromTime(TinkerSet<Time> time) {
		if ( !time.isEmpty() ) {
			this.time.removeAll(time);
		}
	}
	
	public void removeFromUniverse(TinkerSet<Universe> universe) {
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
		if ( space != null ) {
			space.clearSpaceTime();
			space.initialiseProperty(SpaceRuntimePropertyEnum.spaceTime);
		}
		clearSpace();
		addToSpace(space);
	}
	
	public void setTime(Time time) {
		if ( time != null ) {
			time.clearSpaceTime();
			time.initialiseProperty(TimeRuntimePropertyEnum.spaceTime);
		}
		clearTime();
		addToTime(time);
	}
	
	public void setUniverse(Universe universe) {
		if ( universe != null ) {
			universe.clearSpaceTime();
			universe.initialiseProperty(UniverseRuntimePropertyEnum.spaceTime);
		}
		clearUniverse();
		addToUniverse(universe);
	}
	
	@Override
	public String toJson() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"id\": " + getId() + ", ");
		sb.append("\"space\": \"" + getSpace() + "\"");
		sb.append(", ");
		sb.append("\"time\": \"" + getTime() + "\"");
		sb.append(", ");
		sb.append("\"name\": \"" + getName() + "\"");
		sb.append("}");
		return sb.toString();
	}

	static public enum SpaceTimeRuntimePropertyEnum implements TumlRuntimeProperty {
		space(false,false,true,true,"A_<spaceTime>_<space>",true,false,false,false,1,1,false,false,false,false,true,"{\"space\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": true, \"oneToMany\": false, \"manyToOne\": false, \"manyToMany\": false, \"upper\": 1, \"lower\": 1, \"label\": \"A_<spaceTime>_<space>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		time(false,false,true,true,"A_<spaceTime>_<time>",true,false,false,false,1,1,false,false,false,false,true,"{\"time\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": true, \"oneToMany\": false, \"manyToOne\": false, \"manyToMany\": false, \"upper\": 1, \"lower\": 1, \"label\": \"A_<spaceTime>_<time>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		universe(false,false,false,false,"A_<universe>_<spaceTime>",true,false,false,false,1,1,false,false,false,false,true,"{\"universe\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": false, \"composite\": false, \"oneToOne\": true, \"oneToMany\": false, \"manyToOne\": false, \"manyToMany\": false, \"upper\": 1, \"lower\": 1, \"label\": \"A_<universe>_<spaceTime>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		name(true,false,true,false,"tumltest__org__tuml__componenttest__SpaceTime__name",false,false,true,false,1,1,false,false,false,false,true,"{\"name\": {\"onePrimitive\": true, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": false, \"oneToOne\": false, \"oneToMany\": false, \"manyToOne\": true, \"manyToMany\": false, \"upper\": 1, \"lower\": 1, \"label\": \"tumltest__org__tuml__componenttest__SpaceTime__name\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}");
		private boolean onePrimitive;
		private boolean manyPrimitive;
		private boolean controllingSide;
		private boolean composite;
		private String label;
		private boolean oneToOne;
		private boolean oneToMany;
		private boolean manyToOne;
		private boolean manyToMany;
		private int upper;
		private int lower;
		private boolean qualified;
		private boolean inverseQualified;
		private boolean ordered;
		private boolean inverseOrdered;
		private boolean unique;
		private String json;
		/**
		 * constructor for SpaceTimeRuntimePropertyEnum
		 * 
		 * @param onePrimitive 
		 * @param manyPrimitive 
		 * @param controllingSide 
		 * @param composite 
		 * @param label 
		 * @param oneToOne 
		 * @param oneToMany 
		 * @param manyToOne 
		 * @param manyToMany 
		 * @param upper 
		 * @param lower 
		 * @param qualified 
		 * @param inverseQualified 
		 * @param ordered 
		 * @param inverseOrdered 
		 * @param unique 
		 * @param json 
		 */
		private SpaceTimeRuntimePropertyEnum(boolean onePrimitive, boolean manyPrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower, boolean qualified, boolean inverseQualified, boolean ordered, boolean inverseOrdered, boolean unique, String json) {
			this.onePrimitive = onePrimitive;
			this.manyPrimitive = manyPrimitive;
			this.controllingSide = controllingSide;
			this.composite = composite;
			this.label = label;
			this.oneToOne = oneToOne;
			this.oneToMany = oneToMany;
			this.manyToOne = manyToOne;
			this.manyToMany = manyToMany;
			this.upper = upper;
			this.lower = lower;
			this.qualified = qualified;
			this.inverseQualified = inverseQualified;
			this.ordered = ordered;
			this.inverseOrdered = inverseOrdered;
			this.unique = unique;
			this.json = json;
		}
	
		static public String asJson() {
			StringBuilder sb = new StringBuilder();;
			sb.append("{\"SpaceTime\": [");
			sb.append(SpaceTimeRuntimePropertyEnum.space.toJson());
			sb.append(",");
			sb.append(SpaceTimeRuntimePropertyEnum.time.toJson());
			sb.append(",");
			sb.append(SpaceTimeRuntimePropertyEnum.universe.toJson());
			sb.append(",");
			sb.append(SpaceTimeRuntimePropertyEnum.name.toJson());
			sb.append("]}");
			return sb.toString();
		}
		
		static public SpaceTimeRuntimePropertyEnum fromLabel(String label) {
			if ( space.getLabel().equals(label) ) {
				return space;
			}
			if ( time.getLabel().equals(label) ) {
				return time;
			}
			if ( universe.getLabel().equals(label) ) {
				return universe;
			}
			if ( name.getLabel().equals(label) ) {
				return name;
			}
			return null;
		}
		
		public String getJson() {
			return this.json;
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
		
		public boolean isInverseOrdered() {
			return this.inverseOrdered;
		}
		
		public boolean isInverseQualified() {
			return this.inverseQualified;
		}
		
		public boolean isManyPrimitive() {
			return this.manyPrimitive;
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
		
		public boolean isOrdered() {
			return this.ordered;
		}
		
		public boolean isQualified() {
			return this.qualified;
		}
		
		public boolean isUnique() {
			return this.unique;
		}
		
		@Override
		public boolean isValid(int elementCount) {
			if ( isQualified() ) {
				return elementCount >= getLower();
			} else {
				return (getUpper() == -1 || elementCount <= getUpper()) && elementCount >= getLower();
			}
		}
		
		@Override
		public String toJson() {
			return getJson();
		}
	
	}
}