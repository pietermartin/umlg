package org.tuml.qualifier;

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
import org.tuml.qualifier.Angel.AngelRuntimePropertyEnum;
import org.tuml.qualifier.Nature.NatureRuntimePropertyEnum;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.collection.Multiplicity;
import org.tuml.runtime.collection.Qualifier;
import org.tuml.runtime.collection.TinkerQualifiedSet;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.collection.persistent.TinkerQualifiedSetImpl;
import org.tuml.runtime.collection.persistent.TinkerSetImpl;
import org.tuml.runtime.domain.BaseTuml;
import org.tuml.runtime.domain.TumlNode;

public class God extends BaseTuml implements TumlNode {
	static final public long serialVersionUID = 1L;
	private TinkerQualifiedSet<Nature> nature;
	private TinkerSet<String> name;
	private TinkerQualifiedSet<Angel> angel;

	/**
	 * constructor for God
	 * 
	 * @param vertex 
	 */
	public God(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/**
	 * default constructor for God
	 */
	public God() {
	}
	
	/**
	 * constructor for God
	 * 
	 * @param persistent 
	 */
	public God(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		this.vertex.setProperty("className", getClass().getName());
		defaultCreate();
		initialiseProperties();
		initVariables();
		createComponents();
		Edge edge = GraphDb.getDb().addEdge(null, GraphDb.getDb().getRoot(), this.vertex, "root");
		edge.setProperty("inClass", this.getClass().getName());
	}

	public void addToAngel(Angel angel) {
		if ( angel != null ) {
			angel.clearGod();
			angel.initialiseProperty(AngelRuntimePropertyEnum.god);
		}
		if ( angel != null ) {
			this.angel.add(angel);
		}
	}
	
	public void addToAngel(TinkerSet<Angel> angel) {
		for ( Angel a : angel ) {
			this.addToAngel(a);
		}
	}
	
	public void addToName(String name) {
		if ( name != null ) {
			this.name.add(name);
		}
	}
	
	public void addToNature(Nature nature) {
		if ( nature != null ) {
			nature.clearGod();
			nature.initialiseProperty(NatureRuntimePropertyEnum.god);
		}
		if ( nature != null ) {
			this.nature.add(nature);
		}
	}
	
	public void addToNature(TinkerSet<Nature> nature) {
		for ( Nature n : nature ) {
			this.addToNature(n);
		}
	}
	
	public void clearAngel() {
		this.angel.clear();
	}
	
	public void clearName() {
		this.name.clear();
	}
	
	public void clearNature() {
		this.nature.clear();
	}
	
	public void createComponents() {
	}
	
	@Override
	public void delete() {
		for ( Angel child : getAngel() ) {
			child.delete();
		}
		for ( Nature child : getNature() ) {
			child.delete();
		}
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
	
	public TinkerQualifiedSet<Angel> getAngel() {
		return this.angel;
	}
	
	public Angel getAngelForAngelNameQualifierAngelRankQualifier(String angelNameQualifier, Integer angelRankQualifier) {
		Index<Edge> index = GraphDb.getDb().getIndex(getUid() + ":::" + GodRuntimePropertyEnum.angel.getLabel(), Edge.class);
		if ( index==null ) {
			return null;
		} else {
			String indexKey = "angelNameQualifierangelRankQualifier";
			String indexValue = angelNameQualifier==null?"___NULL___":angelNameQualifier;
			indexValue += angelRankQualifier==null?"___NULL___":angelRankQualifier;
			CloseableIterable<Edge> closeableIterable = index.get(indexKey, indexValue);
			Iterator<Edge> iterator = closeableIterable.iterator();
			if ( iterator.hasNext() ) {
				return new Angel(iterator.next().getVertex(Direction.IN));
			} else {
				return null;
			}
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
	
	public TinkerQualifiedSet<Nature> getNature() {
		return this.nature;
	}
	
	public Nature getNatureForNatureQualifier1(String natureQualifier1) {
		Index<Edge> index = GraphDb.getDb().getIndex(getUid() + ":::" + GodRuntimePropertyEnum.nature.getLabel(), Edge.class);
		if ( index==null ) {
			return null;
		} else {
			String indexKey = "natureQualifier1";
			String indexValue = natureQualifier1==null?"___NULL___":natureQualifier1;
			CloseableIterable<Edge> closeableIterable = index.get(indexKey, indexValue);
			Iterator<Edge> iterator = closeableIterable.iterator();
			if ( iterator.hasNext() ) {
				return new Nature(iterator.next().getVertex(Direction.IN));
			} else {
				return null;
			}
		}
	}
	
	@Override
	public int getObjectVersion() {
		return TinkerIdUtilFactory.getIdUtil().getVersion(this.vertex);
	}
	
	public List<Qualifier> getQualifierForAngel(Angel context) {
		List<Qualifier> result = new ArrayList<Qualifier>();
		result.add(new Qualifier(new String[]{"angelNameQualifier", "angelRankQualifier"}, new String[]{context.getAngelNameQualifier() == null ? "___NULL___" : context.getAngelNameQualifier().toString() , context.getAngelRankQualifier() == null ? "___NULL___" : context.getAngelRankQualifier().toString() }, Multiplicity.ZERO_TO_ONE));
		return result;
	}
	
	public List<Qualifier> getQualifierForNature(Nature context) {
		List<Qualifier> result = new ArrayList<Qualifier>();
		result.add(new Qualifier(new String[]{"natureQualifier1"}, new String[]{context.getNatureQualifier1() == null ? "___NULL___" : context.getNatureQualifier1().toString() }, Multiplicity.ZERO_TO_ONE));
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
		GodRuntimePropertyEnum runtimeProperty = GodRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result.isEmpty() ) {
			switch ( runtimeProperty ) {
				case nature:
					result = getQualifierForNature((Nature)node);
				break;
			
				case angel:
					result = getQualifierForAngel((Angel)node);
				break;
			
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
		GodRuntimePropertyEnum runtimeProperty = GodRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result == 0 ) {
			switch ( runtimeProperty ) {
				case nature:
					result = nature.size();
				break;
			
				case name:
					result = name.size();
				break;
			
				case angel:
					result = angel.size();
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
		this.angel =  new TinkerQualifiedSetImpl<Angel>(this, GodRuntimePropertyEnum.angel);
		this.name =  new TinkerSetImpl<String>(this, GodRuntimePropertyEnum.name);
		this.nature =  new TinkerQualifiedSetImpl<Nature>(this, GodRuntimePropertyEnum.nature);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (GodRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case nature:
				this.nature =  new TinkerQualifiedSetImpl<Nature>(this, GodRuntimePropertyEnum.nature);
			break;
		
			case name:
				this.name =  new TinkerSetImpl<String>(this, GodRuntimePropertyEnum.name);
			break;
		
			case angel:
				this.angel =  new TinkerQualifiedSetImpl<Angel>(this, GodRuntimePropertyEnum.angel);
			break;
		
		}
	}
	
	@Override
	public boolean isTinkerRoot() {
		return true;
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
	
	public void removeFromNature(Nature nature) {
		if ( nature != null ) {
			this.nature.remove(nature);
		}
	}
	
	public void removeFromNature(TinkerSet<Nature> nature) {
		if ( !nature.isEmpty() ) {
			this.nature.removeAll(nature);
		}
	}
	
	public void setAngel(TinkerSet<Angel> angel) {
		clearAngel();
		addToAngel(angel);
	}
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}
	
	public void setName(String name) {
		clearName();
		addToName(name);
	}
	
	public void setNature(TinkerSet<Nature> nature) {
		clearNature();
		addToNature(nature);
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

	static public enum GodRuntimePropertyEnum implements TumlRuntimeProperty {
		angel(false,true,true,"A_<god>_<angel>",false,true,false,false,1,0,true,false,false,false,true,"{\"angel\": {\"onePrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": 1, \"lower\": 0, \"label\": \"A_<god>_<angel>\", \"qualified\": true, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		name(true,true,false,"basicmodel__org__tuml__qualifier__God__name",false,false,true,false,1,1,false,false,false,false,true,"{\"name\": {\"onePrimitive\": true, \"controllingSide\": true, \"composite\": false, \"oneToOne\": false, \"oneToMany\": false, \"manyToOne\": true, \"manyToMany\": false, \"upper\": 1, \"lower\": 1, \"label\": \"basicmodel__org__tuml__qualifier__God__name\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		nature(false,true,true,"A_<god>_<nature>",false,true,false,false,1,0,true,false,false,false,true,"{\"nature\": {\"onePrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": 1, \"lower\": 0, \"label\": \"A_<god>_<nature>\", \"qualified\": true, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}");
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
		 * constructor for GodRuntimePropertyEnum
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
		private GodRuntimePropertyEnum(boolean onePrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower, boolean qualified, boolean inverseQualified, boolean ordered, boolean inverseOrdered, boolean unique, String json) {
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
			sb.append("{\"God\": [");
			sb.append(GodRuntimePropertyEnum.angel.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.name.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.nature.toJson());
			sb.append("]}");
			return sb.toString();
		}
		
		static public GodRuntimePropertyEnum fromLabel(String label) {
			if ( angel.getLabel().equals(label) ) {
				return angel;
			}
			if ( name.getLabel().equals(label) ) {
				return name;
			}
			if ( nature.getLabel().equals(label) ) {
				return nature;
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