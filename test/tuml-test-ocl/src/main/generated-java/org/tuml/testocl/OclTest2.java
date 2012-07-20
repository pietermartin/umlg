package org.tuml.testocl;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.collection.Qualifier;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.collection.impl.TinkerSetImpl;
import org.tuml.runtime.domain.BaseTuml;
import org.tuml.runtime.domain.TumlNode;

public class OclTest2 extends BaseTuml implements TumlNode {
	static final public long serialVersionUID = 1L;
	private TinkerSet<String> name;
	private TinkerSet<OclTestCollection> oclTestCollection;
	private TinkerSet<String> name2;

	/**
	 * constructor for OclTest2
	 * 
	 * @param vertex 
	 */
	public OclTest2(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/**
	 * default constructor for OclTest2
	 */
	public OclTest2() {
	}
	
	/**
	 * constructor for OclTest2
	 * 
	 * @param persistent 
	 */
	public OclTest2(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		defaultCreate();
		initialiseProperties();
		initVariables();
		createComponents();
		Edge edge = GraphDb.getDb().addEdge(null, GraphDb.getDb().getRoot(), this.vertex, "root");
		edge.setProperty("inClass", this.getClass().getName());
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
	
	public void addToOclTestCollection(OclTestCollection oclTestCollection) {
		if ( oclTestCollection != null ) {
			this.oclTestCollection.add(oclTestCollection);
		}
	}
	
	public void addToOclTestCollection(TinkerSet<OclTestCollection> oclTestCollection) {
		if ( !oclTestCollection.isEmpty() ) {
			this.oclTestCollection.addAll(oclTestCollection);
		}
	}
	
	public void clearName() {
		this.name.clear();
	}
	
	public void clearName2() {
		this.name2.clear();
	}
	
	public void clearOclTestCollection() {
		this.oclTestCollection.clear();
	}
	
	public void createComponents() {
	}
	
	@Override
	public void delete() {
		GraphDb.getDb().removeVertex(this.vertex);
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
	
	public TinkerSet<OclTestCollection> getOclTestCollection() {
		return this.oclTestCollection;
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
		OclTest2RuntimePropertyEnum runtimeProperty = OclTest2RuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
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
		OclTest2RuntimePropertyEnum runtimeProperty = OclTest2RuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result == 0 ) {
			switch ( runtimeProperty ) {
				case name2:
					result = name2.size();
				break;
			
				case name:
					result = name.size();
				break;
			
				case oclTestCollection:
					result = oclTestCollection.size();
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
		this.oclTestCollection =  new TinkerSetImpl<OclTestCollection>(this, OclTest2RuntimePropertyEnum.oclTestCollection);
		this.name =  new TinkerSetImpl<String>(this, OclTest2RuntimePropertyEnum.name);
		this.name2 =  new TinkerSetImpl<String>(this, OclTest2RuntimePropertyEnum.name2);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (OclTest2RuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case name2:
				this.name2 =  new TinkerSetImpl<String>(this, OclTest2RuntimePropertyEnum.name2);
			break;
		
			case name:
				this.name =  new TinkerSetImpl<String>(this, OclTest2RuntimePropertyEnum.name);
			break;
		
			case oclTestCollection:
				this.oclTestCollection =  new TinkerSetImpl<OclTestCollection>(this, OclTest2RuntimePropertyEnum.oclTestCollection);
			break;
		
		}
	}
	
	@Override
	public boolean isTinkerRoot() {
		return true;
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
	
	public void removeFromOclTestCollection(OclTestCollection oclTestCollection) {
		if ( oclTestCollection != null ) {
			this.oclTestCollection.remove(oclTestCollection);
		}
	}
	
	public void removeFromOclTestCollection(TinkerSet<OclTestCollection> oclTestCollection) {
		if ( !oclTestCollection.isEmpty() ) {
			this.oclTestCollection.removeAll(oclTestCollection);
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
	
	public void setName2(String name2) {
		clearName2();
		addToName2(name2);
	}
	
	public void setOclTestCollection(TinkerSet<OclTestCollection> oclTestCollection) {
		clearOclTestCollection();
		addToOclTestCollection(oclTestCollection);
	}

	public enum OclTest2RuntimePropertyEnum implements TumlRuntimeProperty {
		oclTestCollection(false,true,false,"A_<oclTest2>_<oclTestCollection>",false,true,false,false,-1,0,false,false,false,false,true),
		name(true,true,false,"testoclmodel__org__tuml__testocl__OclTest2__name",false,false,true,false,1,1,false,false,false,false,true),
		name2(true,true,false,"testoclmodel__org__tuml__testocl__OclTest2__name2",false,false,true,false,1,1,false,false,false,false,true);
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
		/**
		 * constructor for OclTest2RuntimePropertyEnum
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
		 */
		private OclTest2RuntimePropertyEnum(boolean onePrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower, boolean qualified, boolean inverseQualified, boolean ordered, boolean inverseOrdered, boolean unique) {
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
		}
	
		static public OclTest2RuntimePropertyEnum fromLabel(String label) {
			if ( oclTestCollection.getLabel().equals(label) ) {
				return oclTestCollection;
			}
			if ( name.getLabel().equals(label) ) {
				return name;
			}
			if ( name2.getLabel().equals(label) ) {
				return name2;
			}
			return null;
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
			return (getUpper() == -1 || elementCount <= getUpper()) && elementCount >= getLower();
		}
	
	}
}