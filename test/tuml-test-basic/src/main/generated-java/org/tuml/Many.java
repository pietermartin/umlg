package org.tuml;

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
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.collection.persistent.TinkerSetImpl;
import org.tuml.runtime.domain.BaseTuml;
import org.tuml.runtime.domain.CompositionNode;
import org.tuml.runtime.domain.TumlNode;

public class Many extends BaseTuml implements CompositionNode {
	static final public long serialVersionUID = 1L;
	private TinkerSet<String> name;
	private TinkerSet<One> one;

	/**
	 * constructor for Many
	 * 
	 * @param compositeOwner 
	 */
	public Many(One compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		initialiseProperties();
		initVariables();
		createComponents();
		addToOne(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/**
	 * constructor for Many
	 * 
	 * @param vertex 
	 */
	public Many(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/**
	 * default constructor for Many
	 */
	public Many() {
	}
	
	/**
	 * constructor for Many
	 * 
	 * @param persistent 
	 */
	public Many(Boolean persistent) {
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
	
	public void addToOne(One one) {
		if ( one != null ) {
			this.one.add(one);
		}
	}
	
	public void clearName() {
		this.name.clear();
	}
	
	public void clearOne() {
		this.one.clear();
	}
	
	public void createComponents() {
	}
	
	@Override
	public void delete() {
		this.one.clear();
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
	
	public One getOne() {
		TinkerSet<One> tmp = this.one;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
	}
	
	@Override
	public TumlNode getOwningObject() {
		return getOne();
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
		ManyRuntimePropertyEnum runtimeProperty = ManyRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
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
	 * getSize is called from the collection in order to update the index used to implement a sequance's index
	 * 
	 * @param tumlRuntimeProperty 
	 */
	@Override
	public int getSize(TumlRuntimeProperty tumlRuntimeProperty) {
		int result = 0;
		ManyRuntimePropertyEnum runtimeProperty = ManyRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result == 0 ) {
			switch ( runtimeProperty ) {
				case name:
					result = name.size();
				break;
			
				case one:
					result = one.size();
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
		this.one =  new TinkerSetImpl<One>(this, ManyRuntimePropertyEnum.one);
		this.name =  new TinkerSetImpl<String>(this, ManyRuntimePropertyEnum.name);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (ManyRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case name:
				this.name =  new TinkerSetImpl<String>(this, ManyRuntimePropertyEnum.name);
			break;
		
			case one:
				this.one =  new TinkerSetImpl<One>(this, ManyRuntimePropertyEnum.one);
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
	
	public void removeFromOne(One one) {
		if ( one != null ) {
			this.one.remove(one);
		}
	}
	
	public void removeFromOne(TinkerSet<One> one) {
		if ( !one.isEmpty() ) {
			this.one.removeAll(one);
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
	
	public void setOne(One one) {
		clearOne();
		addToOne(one);
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

	static public enum ManyRuntimePropertyEnum implements TumlRuntimeProperty {
		one(false,false,false,"A_<one>_<many>",false,false,true,false,1,1,false,false,false,false,true,"{\"one\": {\"onePrimitive\": false, \"controllingSide\": false, \"composite\": false, \"oneToOne\": false, \"oneToMany\": false, \"manyToOne\": true, \"manyToMany\": false, \"upper\": 1, \"lower\": 1, \"label\": \"A_<one>_<many>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		name(true,true,false,"basicmodel__org__tuml__Many__name",false,false,true,false,1,1,false,false,false,false,true,"{\"name\": {\"onePrimitive\": true, \"controllingSide\": true, \"composite\": false, \"oneToOne\": false, \"oneToMany\": false, \"manyToOne\": true, \"manyToMany\": false, \"upper\": 1, \"lower\": 1, \"label\": \"basicmodel__org__tuml__Many__name\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}");
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
		private boolean qualified;
		private boolean inverseQualified;
		private boolean ordered;
		private boolean inverseOrdered;
		private boolean unique;
		private String json;
		/**
		 * constructor for ManyRuntimePropertyEnum
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
		 * @param qualified 
		 * @param inverseQualified 
		 * @param ordered 
		 * @param inverseOrdered 
		 * @param unique 
		 * @param json 
		 */
		private ManyRuntimePropertyEnum(boolean onePrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower, boolean qualified, boolean inverseQualified, boolean ordered, boolean inverseOrdered, boolean unique, String json) {
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
			this.qualified = qualified;
			this.inverseQualified = inverseQualified;
			this.ordered = ordered;
			this.inverseOrdered = inverseOrdered;
			this.unique = unique;
			this.json = json;
		}
	
		static public String asJson() {
			StringBuilder sb = new StringBuilder();;
			sb.append("{\"Many\": [");
			sb.append(ManyRuntimePropertyEnum.one.toJson());
			sb.append(",");
			sb.append(ManyRuntimePropertyEnum.name.toJson());
			sb.append("]}");
			return sb.toString();
		}
		
		static public ManyRuntimePropertyEnum fromLabel(String label) {
			if ( one.getLabel().equals(label) ) {
				return one;
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