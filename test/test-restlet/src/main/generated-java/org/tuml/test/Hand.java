package org.tuml.test;

import com.tinkerpop.blueprints.Vertex;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.codehaus.jackson.map.ObjectMapper;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.collection.Qualifier;
import org.tuml.runtime.collection.TinkerOrderedSet;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.collection.persistent.TinkerOrderedSetImpl;
import org.tuml.runtime.collection.persistent.TinkerSetImpl;
import org.tuml.runtime.domain.BaseTuml;
import org.tuml.runtime.domain.CompositionNode;
import org.tuml.runtime.domain.TumlNode;
import org.tuml.test.Finger.FingerRuntimePropertyEnum;

public class Hand extends BaseTuml implements CompositionNode {
	static final public long serialVersionUID = 1L;
	private TinkerOrderedSet<Finger> finger;
	private TinkerSet<String> name;
	private TinkerSet<Human> human;

	/**
	 * constructor for Hand
	 * 
	 * @param compositeOwner 
	 */
	public Hand(Human compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		initialiseProperties();
		initVariables();
		createComponents();
		addToHuman(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/**
	 * constructor for Hand
	 * 
	 * @param vertex 
	 */
	public Hand(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/**
	 * default constructor for Hand
	 */
	public Hand() {
	}
	
	/**
	 * constructor for Hand
	 * 
	 * @param persistent 
	 */
	public Hand(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		this.vertex.setProperty("className", getClass().getName());
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
		initialiseProperties();
		initVariables();
		createComponents();
	}

	public void addToFinger(Finger finger) {
		if ( finger != null ) {
			finger.clearHand();
			finger.initialiseProperty(FingerRuntimePropertyEnum.hand);
		}
		if ( finger != null ) {
			this.finger.add(finger);
		}
	}
	
	public void addToFinger(TinkerOrderedSet<Finger> finger) {
		if ( !finger.isEmpty() ) {
			this.finger.addAll(finger);
		}
	}
	
	public void addToHuman(Human human) {
		if ( human != null ) {
			this.human.add(human);
		}
	}
	
	public void addToName(String name) {
		if ( name != null ) {
			this.name.add(name);
		}
	}
	
	public void clearFinger() {
		this.finger.clear();
	}
	
	public void clearHuman() {
		this.human.clear();
	}
	
	public void clearName() {
		this.name.clear();
	}
	
	public void createComponents() {
	}
	
	@Override
	public void delete() {
		for ( Finger child : getFinger() ) {
			child.delete();
		}
		this.human.clear();
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
	
	public TinkerOrderedSet<Finger> getFinger() {
		return this.finger;
	}
	
	public Human getHuman() {
		TinkerSet<Human> tmp = this.human;
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
		return getHuman();
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
		HandRuntimePropertyEnum runtimeProperty = HandRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
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
		HandRuntimePropertyEnum runtimeProperty = HandRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result == 0 ) {
			switch ( runtimeProperty ) {
				case finger:
					result = finger.size();
				break;
			
				case name:
					result = name.size();
				break;
			
				case human:
					result = human.size();
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
		this.human =  new TinkerSetImpl<Human>(this, HandRuntimePropertyEnum.human);
		this.name =  new TinkerSetImpl<String>(this, HandRuntimePropertyEnum.name);
		this.finger =  new TinkerOrderedSetImpl<Finger>(this, HandRuntimePropertyEnum.finger);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (HandRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case finger:
				this.finger =  new TinkerOrderedSetImpl<Finger>(this, HandRuntimePropertyEnum.finger);
			break;
		
			case name:
				this.name =  new TinkerSetImpl<String>(this, HandRuntimePropertyEnum.name);
			break;
		
			case human:
				this.human =  new TinkerSetImpl<Human>(this, HandRuntimePropertyEnum.human);
			break;
		
		}
	}
	
	@Override
	public boolean isTinkerRoot() {
		return false;
	}
	
	public void removeFromFinger(Finger finger) {
		if ( finger != null ) {
			this.finger.remove(finger);
		}
	}
	
	public void removeFromFinger(TinkerOrderedSet<Finger> finger) {
		if ( !finger.isEmpty() ) {
			this.finger.removeAll(finger);
		}
	}
	
	public void removeFromHuman(Human human) {
		if ( human != null ) {
			this.human.remove(human);
		}
	}
	
	public void removeFromHuman(TinkerSet<Human> human) {
		if ( !human.isEmpty() ) {
			this.human.removeAll(human);
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
	
	public void setFinger(TinkerOrderedSet<Finger> finger) {
		clearFinger();
		addToFinger(finger);
	}
	
	public void setHuman(Human human) {
		clearHuman();
		addToHuman(human);
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

	static public enum HandRuntimePropertyEnum implements TumlRuntimeProperty {
		human(false,false,false,false,"A_<human>_<hand>",false,false,true,false,1,1,false,false,false,false,true,"{\"human\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": false, \"composite\": false, \"oneToOne\": false, \"oneToMany\": false, \"manyToOne\": true, \"manyToMany\": false, \"upper\": 1, \"lower\": 1, \"label\": \"A_<human>_<hand>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		name(true,false,true,false,"restAndJson__org__tuml__test__Hand__name",false,false,true,false,1,1,false,false,false,false,true,"{\"name\": {\"onePrimitive\": true, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": false, \"oneToOne\": false, \"oneToMany\": false, \"manyToOne\": true, \"manyToMany\": false, \"upper\": 1, \"lower\": 1, \"label\": \"restAndJson__org__tuml__test__Hand__name\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		finger(false,false,true,true,"A_<hand>_<finger>",false,true,false,false,-1,0,false,false,true,false,true,"{\"finger\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": -1, \"lower\": 0, \"label\": \"A_<hand>_<finger>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": true, \"unique\": true}}");
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
		 * constructor for HandRuntimePropertyEnum
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
		private HandRuntimePropertyEnum(boolean onePrimitive, boolean manyPrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower, boolean qualified, boolean inverseQualified, boolean ordered, boolean inverseOrdered, boolean unique, String json) {
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
			sb.append("{\"Hand\": [");
			sb.append(HandRuntimePropertyEnum.human.toJson());
			sb.append(",");
			sb.append(HandRuntimePropertyEnum.name.toJson());
			sb.append(",");
			sb.append(HandRuntimePropertyEnum.finger.toJson());
			sb.append("]}");
			return sb.toString();
		}
		
		static public HandRuntimePropertyEnum fromLabel(String label) {
			if ( human.getLabel().equals(label) ) {
				return human;
			}
			if ( name.getLabel().equals(label) ) {
				return name;
			}
			if ( finger.getLabel().equals(label) ) {
				return finger;
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