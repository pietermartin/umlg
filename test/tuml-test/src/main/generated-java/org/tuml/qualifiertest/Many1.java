package org.tuml.qualifiertest;

import com.tinkerpop.blueprints.CloseableIterable;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Index;
import com.tinkerpop.blueprints.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.codehaus.jackson.map.ObjectMapper;
import org.tuml.concretetest.God;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.collection.Multiplicity;
import org.tuml.runtime.collection.Qualifier;
import org.tuml.runtime.collection.TinkerQualifiedSequence;
import org.tuml.runtime.collection.TinkerQualifiedSet;
import org.tuml.runtime.collection.TinkerSequence;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.collection.persistent.TinkerQualifiedSequenceImpl;
import org.tuml.runtime.collection.persistent.TinkerQualifiedSetImpl;
import org.tuml.runtime.collection.persistent.TinkerSequenceClosableIterableImpl;
import org.tuml.runtime.collection.persistent.TinkerSequenceImpl;
import org.tuml.runtime.collection.persistent.TinkerSetImpl;
import org.tuml.runtime.domain.BaseTuml;
import org.tuml.runtime.domain.CompositionNode;
import org.tuml.runtime.domain.TumlNode;
import org.tuml.runtime.util.TumlCollections;

public class Many1 extends BaseTuml implements CompositionNode {
	static final public long serialVersionUID = 1L;
	private TinkerSet<String> name;
	private TinkerSet<God> god;
	private TinkerQualifiedSet<Many2> many2;
	private TinkerQualifiedSequence<Many2> many2List;
	private TinkerSequence<Many2> many2UnqualifiedList;

	/**
	 * constructor for Many1
	 * 
	 * @param compositeOwner 
	 */
	public Many1(God compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		initialiseProperties();
		initVariables();
		createComponents();
		addToGod(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/**
	 * constructor for Many1
	 * 
	 * @param vertex 
	 */
	public Many1(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/**
	 * default constructor for Many1
	 */
	public Many1() {
	}
	
	/**
	 * constructor for Many1
	 * 
	 * @param persistent 
	 */
	public Many1(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		this.vertex.setProperty("className", getClass().getName());
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
		initialiseProperties();
		initVariables();
		createComponents();
	}

	public void addToGod(God god) {
		if ( god != null ) {
			this.god.add(god);
		}
	}
	
	public void addToMany2(Many2 many2) {
		if ( many2 != null ) {
			this.many2.add(many2);
		}
	}
	
	public void addToMany2(TinkerSet<Many2> many2) {
		for ( Many2 m : many2 ) {
			this.addToMany2(m);
		}
	}
	
	public void addToMany2List(Many2 many2List) {
		if ( many2List != null ) {
			this.many2List.add(many2List);
		}
	}
	
	public void addToMany2List(TinkerSequence<Many2> many2List) {
		for ( Many2 m : many2List ) {
			this.addToMany2List(m);
		}
	}
	
	public void addToMany2UnqualifiedList(Many2 many2UnqualifiedList) {
		if ( many2UnqualifiedList != null ) {
			this.many2UnqualifiedList.add(many2UnqualifiedList);
		}
	}
	
	public void addToMany2UnqualifiedList(TinkerSequence<Many2> many2UnqualifiedList) {
		if ( !many2UnqualifiedList.isEmpty() ) {
			this.many2UnqualifiedList.addAll(many2UnqualifiedList);
		}
	}
	
	public void addToName(String name) {
		if ( name != null ) {
			this.name.add(name);
		}
	}
	
	public void clearGod() {
		this.god.clear();
	}
	
	public void clearMany2() {
		this.many2.clear();
	}
	
	public void clearMany2List() {
		this.many2List.clear();
	}
	
	public void clearMany2UnqualifiedList() {
		this.many2UnqualifiedList.clear();
	}
	
	public void clearName() {
		this.name.clear();
	}
	
	public void createComponents() {
	}
	
	@Override
	public void delete() {
		this.many2UnqualifiedList.clear();
		this.god.clear();
		this.many2.clear();
		this.many2List.clear();
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
	
	/**
	 * Implements the ocl statement for derived property 'listQualifier1'
	 * <pre>
	 * package tumltest::org::tuml::qualifiertest
	 *     context Many1::listQualifier1 : String
	 *     derive: self.name
	 * endpackage
	 * </pre>
	 */
	public String getListQualifier1() {
		return getName();
	}
	
	public TinkerQualifiedSet<Many2> getMany2() {
		return this.many2;
	}
	
	public Many2 getMany2ForQualifier1(String qualifier1) {
		Index<Edge> index = GraphDb.getDb().getIndex(getUid() + ":::" + Many1RuntimePropertyEnum.many2.getLabel(), Edge.class);
		if ( index==null ) {
			return null;
		} else {
			String indexKey = "qualifier1";
			String indexValue = qualifier1==null?"___NULL___":qualifier1;
			CloseableIterable<Edge> closeableIterable = index.get(indexKey, indexValue);
			Iterator<Edge> iterator = closeableIterable.iterator();
			if ( iterator.hasNext() ) {
				return new Many2(iterator.next().getVertex(Direction.IN));
			} else {
				return null;
			}
		}
	}
	
	public TinkerQualifiedSequence<Many2> getMany2List() {
		return this.many2List;
	}
	
	public TinkerSequence<Many2> getMany2ListForListQualifier2(String listQualifier2) {
		Index<Edge> index = GraphDb.getDb().getIndex(getUid() + ":::" + Many1RuntimePropertyEnum.many2List.getLabel(), Edge.class);
		if ( index==null ) {
			return null;
		} else {
			String indexKey = "listQualifier2";
			String indexValue = listQualifier2==null?"___NULL___":listQualifier2;
			CloseableIterable<Edge> closeableIterable = index.get(indexKey, indexValue);
			Iterator<Edge> iterator = closeableIterable.iterator();
			if ( iterator.hasNext() ) {
				return new TinkerSequenceClosableIterableImpl<Many2>(iterator, Many1RuntimePropertyEnum.many2List);
			} else {
				return TumlCollections.emptySequence();
			}
		}
	}
	
	public TinkerSequence<Many2> getMany2UnqualifiedList() {
		return this.many2UnqualifiedList;
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
	 * Implements the ocl statement for derived property 'qualifier1'
	 * <pre>
	 * package tumltest::org::tuml::qualifiertest
	 *     context Many1::qualifier1 : String
	 *     derive: self.name
	 * endpackage
	 * </pre>
	 */
	public String getQualifier1() {
		return getName();
	}
	
	public List<Qualifier> getQualifierForMany2(Many2 context) {
		List<Qualifier> result = new ArrayList<Qualifier>();
		result.add(new Qualifier(new String[]{"qualifier1"}, new String[]{context.getQualifier1() == null ? "___NULL___" : context.getQualifier1().toString() }, Multiplicity.ZERO_TO_ONE));
		return result;
	}
	
	public List<Qualifier> getQualifierForMany2List(Many2 context) {
		List<Qualifier> result = new ArrayList<Qualifier>();
		result.add(new Qualifier(new String[]{"listQualifier2"}, new String[]{context.getListQualifier2() == null ? "___NULL___" : context.getListQualifier2().toString() }, Multiplicity.ZERO_TO_MANY));
		return result;
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
		Many1RuntimePropertyEnum runtimeProperty = Many1RuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result.isEmpty() ) {
			switch ( runtimeProperty ) {
				case many2List:
					result = getQualifierForMany2List((Many2)node);
				break;
			
				case many2:
					result = getQualifierForMany2((Many2)node);
				break;
			
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
		Many1RuntimePropertyEnum runtimeProperty = Many1RuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result == 0 ) {
			switch ( runtimeProperty ) {
				case name:
					result = name.size();
				break;
			
				case many2List:
					result = many2List.size();
				break;
			
				case god:
					result = god.size();
				break;
			
				case many2:
					result = many2.size();
				break;
			
				case many2UnqualifiedList:
					result = many2UnqualifiedList.size();
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
		this.many2UnqualifiedList =  new TinkerSequenceImpl<Many2>(this, Many1RuntimePropertyEnum.many2UnqualifiedList);
		this.many2 =  new TinkerQualifiedSetImpl<Many2>(this, Many1RuntimePropertyEnum.many2);
		this.god =  new TinkerSetImpl<God>(this, Many1RuntimePropertyEnum.god);
		this.many2List =  new TinkerQualifiedSequenceImpl<Many2>(this, Many1RuntimePropertyEnum.many2List);
		this.name =  new TinkerSetImpl<String>(this, Many1RuntimePropertyEnum.name);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (Many1RuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case name:
				this.name =  new TinkerSetImpl<String>(this, Many1RuntimePropertyEnum.name);
			break;
		
			case many2List:
				this.many2List =  new TinkerQualifiedSequenceImpl<Many2>(this, Many1RuntimePropertyEnum.many2List);
			break;
		
			case god:
				this.god =  new TinkerSetImpl<God>(this, Many1RuntimePropertyEnum.god);
			break;
		
			case many2:
				this.many2 =  new TinkerQualifiedSetImpl<Many2>(this, Many1RuntimePropertyEnum.many2);
			break;
		
			case many2UnqualifiedList:
				this.many2UnqualifiedList =  new TinkerSequenceImpl<Many2>(this, Many1RuntimePropertyEnum.many2UnqualifiedList);
			break;
		
		}
	}
	
	@Override
	public boolean isTinkerRoot() {
		return false;
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
	
	public void removeFromMany2(Many2 many2) {
		if ( many2 != null ) {
			this.many2.remove(many2);
		}
	}
	
	public void removeFromMany2(TinkerSet<Many2> many2) {
		if ( !many2.isEmpty() ) {
			this.many2.removeAll(many2);
		}
	}
	
	public void removeFromMany2List(Many2 many2List) {
		if ( many2List != null ) {
			this.many2List.remove(many2List);
		}
	}
	
	public void removeFromMany2List(TinkerSequence<Many2> many2List) {
		if ( !many2List.isEmpty() ) {
			this.many2List.removeAll(many2List);
		}
	}
	
	public void removeFromMany2UnqualifiedList(Many2 many2UnqualifiedList) {
		if ( many2UnqualifiedList != null ) {
			this.many2UnqualifiedList.remove(many2UnqualifiedList);
		}
	}
	
	public void removeFromMany2UnqualifiedList(TinkerSequence<Many2> many2UnqualifiedList) {
		if ( !many2UnqualifiedList.isEmpty() ) {
			this.many2UnqualifiedList.removeAll(many2UnqualifiedList);
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
	
	public void setGod(God god) {
		clearGod();
		addToGod(god);
	}
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}
	
	public void setMany2(TinkerSet<Many2> many2) {
		clearMany2();
		addToMany2(many2);
	}
	
	public void setMany2List(TinkerSequence<Many2> many2List) {
		clearMany2List();
		addToMany2List(many2List);
	}
	
	public void setMany2UnqualifiedList(TinkerSequence<Many2> many2UnqualifiedList) {
		clearMany2UnqualifiedList();
		addToMany2UnqualifiedList(many2UnqualifiedList);
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

	static public enum Many1RuntimePropertyEnum implements TumlRuntimeProperty {
		many2UnqualifiedList(false,false,false,false,"A_<many1>_<many2>_3",false,false,false,true,-1,0,false,false,true,true,false,"{\"many2UnqualifiedList\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": false, \"composite\": false, \"oneToOne\": false, \"oneToMany\": false, \"manyToOne\": false, \"manyToMany\": true, \"upper\": -1, \"lower\": 0, \"label\": \"A_<many1>_<many2>_3\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": true, \"unique\": false}}"),
		many2(false,false,false,false,"A_<many1>_<many2>",false,false,false,true,1,0,true,true,false,false,true,"{\"many2\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": false, \"composite\": false, \"oneToOne\": false, \"oneToMany\": false, \"manyToOne\": false, \"manyToMany\": true, \"upper\": 1, \"lower\": 0, \"label\": \"A_<many1>_<many2>\", \"qualified\": true, \"inverseQualified\": true, \"inverseOrdered\": false, \"unique\": true}}"),
		god(false,false,false,false,"A_<god>_<many1>",false,false,true,false,1,1,false,false,false,false,true,"{\"god\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": false, \"composite\": false, \"oneToOne\": false, \"oneToMany\": false, \"manyToOne\": true, \"manyToMany\": false, \"upper\": 1, \"lower\": 1, \"label\": \"A_<god>_<many1>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		many2List(false,false,false,false,"A_<many1>_<many2>_2",false,false,false,true,-1,0,true,true,true,true,false,"{\"many2List\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": false, \"composite\": false, \"oneToOne\": false, \"oneToMany\": false, \"manyToOne\": false, \"manyToMany\": true, \"upper\": -1, \"lower\": 0, \"label\": \"A_<many1>_<many2>_2\", \"qualified\": true, \"inverseQualified\": true, \"inverseOrdered\": true, \"unique\": false}}"),
		name(true,false,true,false,"tumltest__org__tuml__qualifiertest__Many1__name",false,false,true,false,1,1,false,false,false,false,true,"{\"name\": {\"onePrimitive\": true, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": false, \"oneToOne\": false, \"oneToMany\": false, \"manyToOne\": true, \"manyToMany\": false, \"upper\": 1, \"lower\": 1, \"label\": \"tumltest__org__tuml__qualifiertest__Many1__name\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}");
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
		 * constructor for Many1RuntimePropertyEnum
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
		private Many1RuntimePropertyEnum(boolean onePrimitive, boolean manyPrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower, boolean qualified, boolean inverseQualified, boolean ordered, boolean inverseOrdered, boolean unique, String json) {
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
			sb.append("{\"Many1\": [");
			sb.append(Many1RuntimePropertyEnum.many2UnqualifiedList.toJson());
			sb.append(",");
			sb.append(Many1RuntimePropertyEnum.many2.toJson());
			sb.append(",");
			sb.append(Many1RuntimePropertyEnum.god.toJson());
			sb.append(",");
			sb.append(Many1RuntimePropertyEnum.many2List.toJson());
			sb.append(",");
			sb.append(Many1RuntimePropertyEnum.name.toJson());
			sb.append("]}");
			return sb.toString();
		}
		
		static public Many1RuntimePropertyEnum fromLabel(String label) {
			if ( many2UnqualifiedList.getLabel().equals(label) ) {
				return many2UnqualifiedList;
			}
			if ( many2.getLabel().equals(label) ) {
				return many2;
			}
			if ( god.getLabel().equals(label) ) {
				return god;
			}
			if ( many2List.getLabel().equals(label) ) {
				return many2List;
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