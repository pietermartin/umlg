package org.tuml.test;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.codehaus.jackson.map.ObjectMapper;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.collection.Qualifier;
import org.tuml.runtime.collection.TinkerBag;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.collection.persistent.TinkerBagImpl;
import org.tuml.runtime.collection.persistent.TinkerSetImpl;
import org.tuml.runtime.domain.BaseTuml;
import org.tuml.runtime.domain.TumlNode;
import org.tuml.test.Hand.HandRuntimePropertyEnum;
import org.tuml.test.Ring.RingRuntimePropertyEnum;

public class Human extends BaseTuml implements TumlNode {
	static final public long serialVersionUID = 1L;
	private TinkerSet<Hand> hand;
	private TinkerBag<Ring> ring;
	private TinkerSet<String> name;
	private TinkerSet<String> name2;

	/**
	 * constructor for Human
	 * 
	 * @param vertex 
	 */
	public Human(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/**
	 * default constructor for Human
	 */
	public Human() {
	}
	
	/**
	 * constructor for Human
	 * 
	 * @param persistent 
	 */
	public Human(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		this.vertex.setProperty("className", getClass().getName());
		defaultCreate();
		initialiseProperties();
		initVariables();
		createComponents();
		Edge edge = GraphDb.getDb().addEdge(null, GraphDb.getDb().getRoot(), this.vertex, "root");
		edge.setProperty("inClass", this.getClass().getName());
	}

	public void addToHand(Hand hand) {
		if ( hand != null ) {
			hand.clearHuman();
			hand.initialiseProperty(HandRuntimePropertyEnum.human);
		}
		if ( hand != null ) {
			this.hand.add(hand);
		}
	}
	
	public void addToHand(TinkerSet<Hand> hand) {
		if ( !hand.isEmpty() ) {
			this.hand.addAll(hand);
		}
	}
	
	public void addToName(String name) {
		if ( name != null ) {
			this.name.add(name);
		}
	}
	
	public void addToName2(String name2) {
		if ( name2 != null ) {
			this.name2.add(name2);
		}
	}
	
	public void addToRing(Ring ring) {
		if ( ring != null ) {
			ring.clearHuman();
			ring.initialiseProperty(RingRuntimePropertyEnum.human);
		}
		if ( ring != null ) {
			this.ring.add(ring);
		}
	}
	
	public void addToRing(TinkerBag<Ring> ring) {
		if ( !ring.isEmpty() ) {
			this.ring.addAll(ring);
		}
	}
	
	public void clearHand() {
		this.hand.clear();
	}
	
	public void clearName() {
		this.name.clear();
	}
	
	public void clearName2() {
		this.name2.clear();
	}
	
	public void clearRing() {
		this.ring.clear();
	}
	
	public void createComponents() {
	}
	
	@Override
	public void delete() {
		for ( Ring child : getRing() ) {
			child.delete();
		}
		for ( Hand child : getHand() ) {
			child.delete();
		}
		GraphDb.getDb().removeVertex(this.vertex);
	}
	
	@Override
	public void fromJson(Map<String,Object> propertyMap) {
		for ( String propertyName : propertyMap.keySet() ) {
			if ( propertyName.equals("name2") ) {
				setName2((String)propertyMap.get(propertyName));
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
	
	public TinkerSet<Hand> getHand() {
		return this.hand;
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
	
	public String getName2() {
		TinkerSet<String> tmp = this.name2;
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
	
	/**
	 * getQualifiers is called from the collection in order to update the index used to implement the qualifier
	 * 
	 * @param tumlRuntimeProperty 
	 * @param node 
	 */
	@Override
	public List<Qualifier> getQualifiers(TumlRuntimeProperty tumlRuntimeProperty, TumlNode node) {
		List<Qualifier> result = Collections.emptyList();
		HumanRuntimePropertyEnum runtimeProperty = HumanRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result.isEmpty() ) {
			switch ( runtimeProperty ) {
				default:
					result = Collections.emptyList();
				break;
			}
		
		}
		return result;
	}
	
	public TinkerBag<Ring> getRing() {
		return this.ring;
	}
	
	/**
	 * getSize is called from the collection in order to update the index used to implement a sequence's index
	 * 
	 * @param tumlRuntimeProperty 
	 */
	@Override
	public int getSize(TumlRuntimeProperty tumlRuntimeProperty) {
		int result = 0;
		HumanRuntimePropertyEnum runtimeProperty = HumanRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result == 0 ) {
			switch ( runtimeProperty ) {
				case hand:
					result = hand.size();
				break;
			
				case name:
					result = name.size();
				break;
			
				case name2:
					result = name2.size();
				break;
			
				case ring:
					result = ring.size();
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
		this.ring =  new TinkerBagImpl<Ring>(this, HumanRuntimePropertyEnum.ring);
		this.name2 =  new TinkerSetImpl<String>(this, HumanRuntimePropertyEnum.name2);
		this.name =  new TinkerSetImpl<String>(this, HumanRuntimePropertyEnum.name);
		this.hand =  new TinkerSetImpl<Hand>(this, HumanRuntimePropertyEnum.hand);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (HumanRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case hand:
				this.hand =  new TinkerSetImpl<Hand>(this, HumanRuntimePropertyEnum.hand);
			break;
		
			case name:
				this.name =  new TinkerSetImpl<String>(this, HumanRuntimePropertyEnum.name);
			break;
		
			case name2:
				this.name2 =  new TinkerSetImpl<String>(this, HumanRuntimePropertyEnum.name2);
			break;
		
			case ring:
				this.ring =  new TinkerBagImpl<Ring>(this, HumanRuntimePropertyEnum.ring);
			break;
		
		}
	}
	
	@Override
	public boolean isTinkerRoot() {
		return true;
	}
	
	public void removeFromHand(Hand hand) {
		if ( hand != null ) {
			this.hand.remove(hand);
		}
	}
	
	public void removeFromHand(TinkerSet<Hand> hand) {
		if ( !hand.isEmpty() ) {
			this.hand.removeAll(hand);
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
	
	public void removeFromName2(String name2) {
		if ( name2 != null ) {
			this.name2.remove(name2);
		}
	}
	
	public void removeFromName2(TinkerSet<String> name2) {
		if ( !name2.isEmpty() ) {
			this.name2.removeAll(name2);
		}
	}
	
	public void removeFromRing(Ring ring) {
		if ( ring != null ) {
			this.ring.remove(ring);
		}
	}
	
	public void removeFromRing(TinkerBag<Ring> ring) {
		if ( !ring.isEmpty() ) {
			this.ring.removeAll(ring);
		}
	}
	
	public void setHand(TinkerSet<Hand> hand) {
		clearHand();
		addToHand(hand);
	}
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}
	
	public void setName(String name) {
		clearName();
		addToName(name);
	}
	
	public void setName2(String name2) {
		clearName2();
		addToName2(name2);
	}
	
	public void setRing(TinkerBag<Ring> ring) {
		clearRing();
		addToRing(ring);
	}
	
	@Override
	public String toJson() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"id\": " + getId() + ", ");
		sb.append("\"name2\": \"" + getName2() + "\"");
		sb.append(", ");
		sb.append("\"name\": \"" + getName() + "\"");
		sb.append("}");
		return sb.toString();
	}

	static public enum HumanRuntimePropertyEnum implements TumlRuntimeProperty {
		ring(false,false,true,true,"A_<human>_<ring>",false,true,false,false,-1,0,false,false,false,false,false,"{\"ring\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": -1, \"lower\": 0, \"label\": \"A_<human>_<ring>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": false}}"),
		name2(true,false,true,false,"restAndJson__org__tuml__test__Human__name2",false,false,true,false,1,1,false,false,false,false,true,"{\"name2\": {\"onePrimitive\": true, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": false, \"oneToOne\": false, \"oneToMany\": false, \"manyToOne\": true, \"manyToMany\": false, \"upper\": 1, \"lower\": 1, \"label\": \"restAndJson__org__tuml__test__Human__name2\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		name(true,false,true,false,"restAndJson__org__tuml__test__Human__name",false,false,true,false,1,1,false,false,false,false,true,"{\"name\": {\"onePrimitive\": true, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": false, \"oneToOne\": false, \"oneToMany\": false, \"manyToOne\": true, \"manyToMany\": false, \"upper\": 1, \"lower\": 1, \"label\": \"restAndJson__org__tuml__test__Human__name\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		hand(false,false,true,true,"A_<human>_<hand>",false,true,false,false,-1,0,false,false,false,false,true,"{\"hand\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": -1, \"lower\": 0, \"label\": \"A_<human>_<hand>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}");
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
		 * constructor for HumanRuntimePropertyEnum
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
		private HumanRuntimePropertyEnum(boolean onePrimitive, boolean manyPrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower, boolean qualified, boolean inverseQualified, boolean ordered, boolean inverseOrdered, boolean unique, String json) {
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
			sb.append("{\"Human\": [");
			sb.append(HumanRuntimePropertyEnum.ring.toJson());
			sb.append(",");
			sb.append(HumanRuntimePropertyEnum.name2.toJson());
			sb.append(",");
			sb.append(HumanRuntimePropertyEnum.name.toJson());
			sb.append(",");
			sb.append(HumanRuntimePropertyEnum.hand.toJson());
			sb.append("]}");
			return sb.toString();
		}
		
		static public HumanRuntimePropertyEnum fromLabel(String label) {
			if ( ring.getLabel().equals(label) ) {
				return ring;
			}
			if ( name2.getLabel().equals(label) ) {
				return name2;
			}
			if ( name.getLabel().equals(label) ) {
				return name;
			}
			if ( hand.getLabel().equals(label) ) {
				return hand;
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