package org.tuml.concretetest;

import com.tinkerpop.blueprints.Vertex;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.codehaus.jackson.map.ObjectMapper;
import org.tuml.componenttest.SpaceTime;
import org.tuml.componenttest.SpaceTime.SpaceTimeRuntimePropertyEnum;
import org.tuml.concretetest.Angel.AngelRuntimePropertyEnum;
import org.tuml.concretetest.Demon.DemonRuntimePropertyEnum;
import org.tuml.navigability.NonNavigableMany;
import org.tuml.navigability.NonNavigableMany.NonNavigableManyRuntimePropertyEnum;
import org.tuml.navigability.NonNavigableOne;
import org.tuml.navigability.NonNavigableOne.NonNavigableOneRuntimePropertyEnum;
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

public class Universe extends BaseTuml implements CompositionNode {
	static final public long serialVersionUID = 1L;
	private TinkerSet<String> name;
	private TinkerSet<SpaceTime> spaceTime;
	private TinkerSet<God> god;
	private TinkerSet<Angel> angel;
	private TinkerSet<Demon> demon;
	private TinkerSet<NonNavigableOne> nonNavigableOne;
	private TinkerSet<NonNavigableMany> nonNavigableMany;

	/**
	 * constructor for Universe
	 * 
	 * @param compositeOwner 
	 */
	public Universe(God compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		initialiseProperties();
		initVariables();
		createComponents();
		addToGod(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/**
	 * constructor for Universe
	 * 
	 * @param vertex 
	 */
	public Universe(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/**
	 * default constructor for Universe
	 */
	public Universe() {
	}
	
	/**
	 * constructor for Universe
	 * 
	 * @param persistent 
	 */
	public Universe(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		this.vertex.setProperty("className", getClass().getName());
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
		initialiseProperties();
		initVariables();
		createComponents();
	}

	public void addToAngel(Angel angel) {
		if ( angel != null ) {
			this.angel.add(angel);
		}
	}
	
	public void addToDemon(Demon demon) {
		if ( demon != null ) {
			demon.clearUniverse();
			demon.initialiseProperty(DemonRuntimePropertyEnum.universe);
		}
		if ( demon != null ) {
			this.demon.add(demon);
		}
	}
	
	public void addToDemon(TinkerSet<Demon> demon) {
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
			nonNavigableMany.clearUniverse();
			nonNavigableMany.initialiseProperty(NonNavigableManyRuntimePropertyEnum.universe);
		}
		if ( nonNavigableMany != null ) {
			this.nonNavigableMany.add(nonNavigableMany);
		}
	}
	
	public void addToNonNavigableMany(TinkerSet<NonNavigableMany> nonNavigableMany) {
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
		if ( getSpaceTime() != null ) {
			getSpaceTime().delete();
		}
		this.demon.clear();
		this.god.clear();
		this.nonNavigableOne.clear();
		this.angel.clear();
		this.nonNavigableMany.clear();
		GraphDb.getDb().removeVertex(this.vertex);
	}
	
	@Override
	public void fromJson(Map<String,Object> propertyMap) {
		for ( String propertyName : propertyMap.keySet() ) {
			if ( propertyName.equals("spaceTime") ) {
				setSpaceTime((org.tuml.componenttest.SpaceTime)propertyMap.get(propertyName));
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
	public TumlNode getOwningObject() {
		return getGod();
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
		UniverseRuntimePropertyEnum runtimeProperty = UniverseRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
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
		UniverseRuntimePropertyEnum runtimeProperty = UniverseRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result == 0 ) {
			switch ( runtimeProperty ) {
				case name:
					result = name.size();
				break;
			
				case nonNavigableMany:
					result = nonNavigableMany.size();
				break;
			
				case angel:
					result = angel.size();
				break;
			
				case nonNavigableOne:
					result = nonNavigableOne.size();
				break;
			
				case god:
					result = god.size();
				break;
			
				case spaceTime:
					result = spaceTime.size();
				break;
			
				case demon:
					result = demon.size();
				break;
			
				default:
					result = 0;
				break;
			}
		
		}
		return result;
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
	
	public void initVariables() {
	}
	
	@Override
	public void initialiseProperties() {
		this.demon =  new TinkerSetImpl<Demon>(this, UniverseRuntimePropertyEnum.demon);
		this.spaceTime =  new TinkerSetImpl<SpaceTime>(this, UniverseRuntimePropertyEnum.spaceTime);
		this.god =  new TinkerSetImpl<God>(this, UniverseRuntimePropertyEnum.god);
		this.nonNavigableOne =  new TinkerSetImpl<NonNavigableOne>(this, UniverseRuntimePropertyEnum.nonNavigableOne);
		this.angel =  new TinkerSetImpl<Angel>(this, UniverseRuntimePropertyEnum.angel);
		this.nonNavigableMany =  new TinkerSetImpl<NonNavigableMany>(this, UniverseRuntimePropertyEnum.nonNavigableMany);
		this.name =  new TinkerSetImpl<String>(this, UniverseRuntimePropertyEnum.name);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (UniverseRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case name:
				this.name =  new TinkerSetImpl<String>(this, UniverseRuntimePropertyEnum.name);
			break;
		
			case nonNavigableMany:
				this.nonNavigableMany =  new TinkerSetImpl<NonNavigableMany>(this, UniverseRuntimePropertyEnum.nonNavigableMany);
			break;
		
			case angel:
				this.angel =  new TinkerSetImpl<Angel>(this, UniverseRuntimePropertyEnum.angel);
			break;
		
			case nonNavigableOne:
				this.nonNavigableOne =  new TinkerSetImpl<NonNavigableOne>(this, UniverseRuntimePropertyEnum.nonNavigableOne);
			break;
		
			case god:
				this.god =  new TinkerSetImpl<God>(this, UniverseRuntimePropertyEnum.god);
			break;
		
			case spaceTime:
				this.spaceTime =  new TinkerSetImpl<SpaceTime>(this, UniverseRuntimePropertyEnum.spaceTime);
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
	
	public void removeFromAngel(TinkerSet<Angel> angel) {
		if ( !angel.isEmpty() ) {
			this.angel.removeAll(angel);
		}
	}
	
	public void removeFromDemon(Demon demon) {
		if ( demon != null ) {
			this.demon.remove(demon);
		}
	}
	
	public void removeFromDemon(TinkerSet<Demon> demon) {
		if ( !demon.isEmpty() ) {
			this.demon.removeAll(demon);
		}
	}
	
	public void removeFromGod(God god) {
		if ( god != null ) {
			this.god.remove(god);
		}
	}
	
	public void removeFromGod(TinkerSet<God> god) {
		if ( !god.isEmpty() ) {
			this.god.removeAll(god);
		}
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
	
	public void removeFromNonNavigableMany(NonNavigableMany nonNavigableMany) {
		if ( nonNavigableMany != null ) {
			this.nonNavigableMany.remove(nonNavigableMany);
		}
	}
	
	public void removeFromNonNavigableMany(TinkerSet<NonNavigableMany> nonNavigableMany) {
		if ( !nonNavigableMany.isEmpty() ) {
			this.nonNavigableMany.removeAll(nonNavigableMany);
		}
	}
	
	public void removeFromNonNavigableOne(NonNavigableOne nonNavigableOne) {
		if ( nonNavigableOne != null ) {
			this.nonNavigableOne.remove(nonNavigableOne);
		}
	}
	
	public void removeFromNonNavigableOne(TinkerSet<NonNavigableOne> nonNavigableOne) {
		if ( !nonNavigableOne.isEmpty() ) {
			this.nonNavigableOne.removeAll(nonNavigableOne);
		}
	}
	
	public void removeFromSpaceTime(SpaceTime spaceTime) {
		if ( spaceTime != null ) {
			this.spaceTime.remove(spaceTime);
		}
	}
	
	public void removeFromSpaceTime(TinkerSet<SpaceTime> spaceTime) {
		if ( !spaceTime.isEmpty() ) {
			this.spaceTime.removeAll(spaceTime);
		}
	}
	
	public void setAngel(Angel angel) {
		if ( angel != null ) {
			angel.clearUniverse();
			angel.initialiseProperty(AngelRuntimePropertyEnum.universe);
		}
		clearAngel();
		addToAngel(angel);
	}
	
	public void setDemon(TinkerSet<Demon> demon) {
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
	
	public void setNonNavigableMany(TinkerSet<NonNavigableMany> nonNavigableMany) {
		clearNonNavigableMany();
		addToNonNavigableMany(nonNavigableMany);
	}
	
	public void setNonNavigableOne(NonNavigableOne nonNavigableOne) {
		if ( nonNavigableOne != null ) {
			nonNavigableOne.clearUniverse();
			nonNavigableOne.initialiseProperty(NonNavigableOneRuntimePropertyEnum.universe);
		}
		clearNonNavigableOne();
		addToNonNavigableOne(nonNavigableOne);
	}
	
	public void setSpaceTime(SpaceTime spaceTime) {
		if ( spaceTime != null ) {
			spaceTime.clearUniverse();
			spaceTime.initialiseProperty(SpaceTimeRuntimePropertyEnum.universe);
		}
		clearSpaceTime();
		addToSpaceTime(spaceTime);
	}
	
	@Override
	public String toJson() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"id\": " + getId() + ", ");
		sb.append("\"spaceTime\": \"" + getSpaceTime() + "\"");
		sb.append(", ");
		sb.append("\"name\": \"" + getName() + "\"");
		sb.append("}");
		return sb.toString();
	}

	static public enum UniverseRuntimePropertyEnum implements TumlRuntimeProperty {
		demon(false,false,true,false,"A_<universe>_<demon>",false,true,false,false,-1,1,false,false,false,false,true,"{\"demon\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": false, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": -1, \"lower\": 1, \"label\": \"A_<universe>_<demon>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		spaceTime(false,false,true,true,"A_<universe>_<spaceTime>",true,false,false,false,1,1,false,false,false,false,true,"{\"spaceTime\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": true, \"oneToMany\": false, \"manyToOne\": false, \"manyToMany\": false, \"upper\": 1, \"lower\": 1, \"label\": \"A_<universe>_<spaceTime>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		god(false,false,false,false,"A_<god>_<universe>",false,false,true,false,1,1,false,false,false,false,true,"{\"god\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": false, \"composite\": false, \"oneToOne\": false, \"oneToMany\": false, \"manyToOne\": true, \"manyToMany\": false, \"upper\": 1, \"lower\": 1, \"label\": \"A_<god>_<universe>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		nonNavigableOne(false,false,false,false,"A_<universe>_<nonNavigableOne>",true,false,false,false,1,0,false,false,false,false,true,"{\"nonNavigableOne\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": false, \"composite\": false, \"oneToOne\": true, \"oneToMany\": false, \"manyToOne\": false, \"manyToMany\": false, \"upper\": 1, \"lower\": 0, \"label\": \"A_<universe>_<nonNavigableOne>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		angel(false,false,false,false,"A_<universe>_<angel>",true,false,false,false,1,0,false,false,false,false,true,"{\"angel\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": false, \"composite\": false, \"oneToOne\": true, \"oneToMany\": false, \"manyToOne\": false, \"manyToMany\": false, \"upper\": 1, \"lower\": 0, \"label\": \"A_<universe>_<angel>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		nonNavigableMany(false,false,true,false,"A_<universe>_<nonNavigableMany>",false,true,false,false,-1,0,false,false,false,false,true,"{\"nonNavigableMany\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": false, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": -1, \"lower\": 0, \"label\": \"A_<universe>_<nonNavigableMany>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		name(true,false,true,false,"tumltest__org__tuml__concretetest__Universe__name",false,false,true,false,1,1,false,false,false,false,true,"{\"name\": {\"onePrimitive\": true, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": false, \"oneToOne\": false, \"oneToMany\": false, \"manyToOne\": true, \"manyToMany\": false, \"upper\": 1, \"lower\": 1, \"label\": \"tumltest__org__tuml__concretetest__Universe__name\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}");
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
		 * constructor for UniverseRuntimePropertyEnum
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
		private UniverseRuntimePropertyEnum(boolean onePrimitive, boolean manyPrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower, boolean qualified, boolean inverseQualified, boolean ordered, boolean inverseOrdered, boolean unique, String json) {
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
			sb.append("{\"Universe\": [");
			sb.append(UniverseRuntimePropertyEnum.demon.toJson());
			sb.append(",");
			sb.append(UniverseRuntimePropertyEnum.spaceTime.toJson());
			sb.append(",");
			sb.append(UniverseRuntimePropertyEnum.god.toJson());
			sb.append(",");
			sb.append(UniverseRuntimePropertyEnum.nonNavigableOne.toJson());
			sb.append(",");
			sb.append(UniverseRuntimePropertyEnum.angel.toJson());
			sb.append(",");
			sb.append(UniverseRuntimePropertyEnum.nonNavigableMany.toJson());
			sb.append(",");
			sb.append(UniverseRuntimePropertyEnum.name.toJson());
			sb.append("]}");
			return sb.toString();
		}
		
		static public UniverseRuntimePropertyEnum fromLabel(String label) {
			if ( demon.getLabel().equals(label) ) {
				return demon;
			}
			if ( spaceTime.getLabel().equals(label) ) {
				return spaceTime;
			}
			if ( god.getLabel().equals(label) ) {
				return god;
			}
			if ( nonNavigableOne.getLabel().equals(label) ) {
				return nonNavigableOne;
			}
			if ( angel.getLabel().equals(label) ) {
				return angel;
			}
			if ( nonNavigableMany.getLabel().equals(label) ) {
				return nonNavigableMany;
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