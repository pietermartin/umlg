package org.tuml.interfacetest;

import com.tinkerpop.blueprints.Vertex;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.codehaus.jackson.map.ObjectMapper;
import org.tuml.concretetest.God;
import org.tuml.interfacetest.Creature.CreatureRuntimePropertyEnum;
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

public class Spook extends BaseTuml implements CompositionNode, Spirit {
	static final public long serialVersionUID = 1L;
	private TinkerSet<String> name;
	private TinkerSet<Creature> creature;
	private TinkerSet<God> god;

	/**
	 * constructor for Spook
	 * 
	 * @param compositeOwner 
	 */
	public Spook(God compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		initialiseProperties();
		initVariables();
		createComponents();
		addToGod(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/**
	 * constructor for Spook
	 * 
	 * @param vertex 
	 */
	public Spook(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/**
	 * default constructor for Spook
	 */
	public Spook() {
	}
	
	/**
	 * constructor for Spook
	 * 
	 * @param persistent 
	 */
	public Spook(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		this.vertex.setProperty("className", getClass().getName());
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
		initialiseProperties();
		initVariables();
		createComponents();
	}

	public void addToCreature(Creature creature) {
		if ( creature != null ) {
			this.creature.add(creature);
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
	
	public void clearCreature() {
		this.creature.clear();
	}
	
	public void clearGod() {
		this.god.clear();
	}
	
	public void clearName() {
		this.name.clear();
	}
	
	public void createComponents() {
	}
	
	@Override
	public void delete() {
		this.creature.clear();
		this.god.clear();
		GraphDb.getDb().removeVertex(this.vertex);
	}
	
	@Override
	public void fromJson(Map<String,Object> propertyMap) {
		for ( String propertyName : propertyMap.keySet() ) {
			if ( propertyName.equals("name") ) {
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
	
	public Creature getCreature() {
		TinkerSet<Creature> tmp = this.creature;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
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
		SpookRuntimePropertyEnum runtimeProperty = SpookRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
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
		SpookRuntimePropertyEnum runtimeProperty = SpookRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result == 0 ) {
			switch ( runtimeProperty ) {
				case god:
					result = god.size();
				break;
			
				case creature:
					result = creature.size();
				break;
			
				case name:
					result = name.size();
				break;
			
				default:
					result = 0;
				break;
			}
		
		}
		return result;
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
		this.name =  new TinkerSetImpl<String>(this, SpookRuntimePropertyEnum.name);
		this.creature =  new TinkerSetImpl<Creature>(this, SpookRuntimePropertyEnum.creature);
		this.god =  new TinkerSetImpl<God>(this, SpookRuntimePropertyEnum.god);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (SpookRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case god:
				this.god =  new TinkerSetImpl<God>(this, SpookRuntimePropertyEnum.god);
			break;
		
			case creature:
				this.creature =  new TinkerSetImpl<Creature>(this, SpookRuntimePropertyEnum.creature);
			break;
		
			case name:
				this.name =  new TinkerSetImpl<String>(this, SpookRuntimePropertyEnum.name);
			break;
		
		}
	}
	
	@Override
	public boolean isTinkerRoot() {
		return false;
	}
	
	public void removeFromCreature(Creature creature) {
		if ( creature != null ) {
			this.creature.remove(creature);
		}
	}
	
	public void removeFromCreature(TinkerSet<Creature> creature) {
		if ( !creature.isEmpty() ) {
			this.creature.removeAll(creature);
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
	
	public void setCreature(Creature creature) {
		if ( creature != null ) {
			creature.clearSpook();
			creature.initialiseProperty(CreatureRuntimePropertyEnum.spook);
		}
		clearCreature();
		addToCreature(creature);
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
	
	@Override
	public String toJson() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"id\": " + getId() + ", ");
		sb.append("\"name\": \"" + getName() + "\"");
		sb.append("}");
		return sb.toString();
	}

	static public enum SpookRuntimePropertyEnum implements TumlRuntimeProperty {
		name(true,false,true,false,"tumltest__org__tuml__interfacetest__Spook__name",false,false,true,false,1,1,false,false,false,false,true,"{\"name\": {\"onePrimitive\": true, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": false, \"oneToOne\": false, \"oneToMany\": false, \"manyToOne\": true, \"manyToMany\": false, \"upper\": 1, \"lower\": 1, \"label\": \"tumltest__org__tuml__interfacetest__Spook__name\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		creature(false,false,true,false,"A_<spook>_<creature>",true,false,false,false,1,1,false,false,false,false,true,"{\"creature\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": false, \"oneToOne\": true, \"oneToMany\": false, \"manyToOne\": false, \"manyToMany\": false, \"upper\": 1, \"lower\": 1, \"label\": \"A_<spook>_<creature>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		god(false,false,false,false,"A_<god>_<spirit>",false,false,true,false,1,1,false,false,false,false,true,"{\"god\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": false, \"composite\": false, \"oneToOne\": false, \"oneToMany\": false, \"manyToOne\": true, \"manyToMany\": false, \"upper\": 1, \"lower\": 1, \"label\": \"A_<god>_<spirit>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}");
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
		 * constructor for SpookRuntimePropertyEnum
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
		private SpookRuntimePropertyEnum(boolean onePrimitive, boolean manyPrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower, boolean qualified, boolean inverseQualified, boolean ordered, boolean inverseOrdered, boolean unique, String json) {
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
			sb.append("{\"Spook\": [");
			sb.append(SpookRuntimePropertyEnum.name.toJson());
			sb.append(",");
			sb.append(SpookRuntimePropertyEnum.creature.toJson());
			sb.append(",");
			sb.append(SpookRuntimePropertyEnum.god.toJson());
			sb.append("]}");
			return sb.toString();
		}
		
		static public SpookRuntimePropertyEnum fromLabel(String label) {
			if ( name.getLabel().equals(label) ) {
				return name;
			}
			if ( creature.getLabel().equals(label) ) {
				return creature;
			}
			if ( god.getLabel().equals(label) ) {
				return god;
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