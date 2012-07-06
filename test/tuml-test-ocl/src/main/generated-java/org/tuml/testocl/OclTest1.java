package org.tuml.testocl;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.collection.Qualifier;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TinkerSetImpl;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.TinkerNode;

public class OclTest1 extends BaseTinker implements TinkerNode {
	static final public long serialVersionUID = 1L;
	private TinkerSet<String> property1;
	private TinkerSet<OclTestCollection> oclTestCollection;

	/** Constructor for OclTest1
	 * 
	 * @param vertex 
	 */
	public OclTest1(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/** Default constructor for OclTest1
	 */
	public OclTest1() {
	}
	
	/** Constructor for OclTest1
	 * 
	 * @param persistent 
	 */
	public OclTest1(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		defaultCreate();
		initialiseProperties();
		initVariables();
		createComponents();
		Edge edge = GraphDb.getDb().addEdge(null, GraphDb.getDb().getRoot(), this.vertex, "root");
		edge.setProperty("inClass", this.getClass().getName());
	}

	public void addToOclTestCollection(OclTestCollection oclTestCollection) {
		if ( oclTestCollection != null ) {
			this.oclTestCollection.add(oclTestCollection);
		}
	}
	
	public void addToOclTestCollection(Set<OclTestCollection> oclTestCollection) {
		if ( !oclTestCollection.isEmpty() ) {
			this.oclTestCollection.addAll(oclTestCollection);
		}
	}
	
	public void addToProperty1(String property1) {
		if ( property1 != null ) {
			this.property1.add(property1);
		}
	}
	
	public void clearOclTestCollection() {
		this.oclTestCollection.clear();
	}
	
	public void clearProperty1() {
		this.property1.clear();
	}
	
	public void createComponents() {
	}
	
	@Override
	public void delete() {
		for ( OclTestCollection child : getOclTestCollection() ) {
			child.delete();
		}
		GraphDb.getDb().removeVertex(this.vertex);
	}
	
	public String getDerivedProperty1() {
		return getProperty1();
	}
	
	@Override
	public Long getId() {
		return TinkerIdUtilFactory.getIdUtil().getId(this.vertex);
	}
	
	@Override
	public int getObjectVersion() {
		return TinkerIdUtilFactory.getIdUtil().getVersion(this.vertex);
	}
	
	public OclTestCollection getOclTestAny() {
		org.tuml.testocl.OclTestCollection result1;
		result1 = null;
		
		/* Iterator Any: Iterate through the elements and return one element that fulfills the condition. */
		for (org.tuml.testocl.OclTestCollection anElement1 : getOclTestCollection()) {
		    if (!anElement1.getName().equals("john")) {
		        result1 = anElement1;
		        break;
		    }
		    // no else
		}
		
		return result1;
	}
	
	public TinkerSet<OclTestCollection> getOclTestCollection() {
		return this.oclTestCollection;
	}
	
	public Set<OclTestCollection2> getOclTestCollection2() {
		java.util.ArrayList<org.tuml.testocl.OclTestCollection2> result1;
		result1 = new java.util.ArrayList<org.tuml.testocl.OclTestCollection2>();
		
		/* Iterator Collect: Iterate through all elements and collect them. Elements which are collections are flattened. */
		for (org.tuml.testocl.OclTestCollection anElement1 : getOclTestCollection()) {
		    result1.addAll(anElement1.getOclTestCollection2());
		}
		
		return tudresden.ocl20.pivot.tools.codegen.ocl2java.types.util.OclBags.asSet(result1);
	}
	
	public Collection<String> getOclTestCollection2Name() {
		java.util.ArrayList<org.tuml.testocl.OclTestCollection2> result2;
		result2 = new java.util.ArrayList<org.tuml.testocl.OclTestCollection2>();
		
		/* Iterator Collect: Iterate through all elements and collect them. Elements which are collections are flattened. */
		for (org.tuml.testocl.OclTestCollection anElement1 : getOclTestCollection()) {
		    result2.addAll(anElement1.getOclTestCollection2());
		}
		java.util.ArrayList<String> result1;
		result1 = new java.util.ArrayList<String>();
		
		/* Iterator Collect: Iterate through all elements and collect them. Elements which are collections are flattened. */
		for (org.tuml.testocl.OclTestCollection2 anElement2 : result2) {
		    result1.add(anElement2.getName());
		}
		
		return result1;
	}
	
	public Set<OclTestCollection> getOclTestCollectionSelect() {
		java.util.HashSet<org.tuml.testocl.OclTestCollection> result1;
		result1 = new java.util.HashSet<org.tuml.testocl.OclTestCollection>();
		
		/* Iterator Select: Select all elements which fulfill the condition. */
		for (org.tuml.testocl.OclTestCollection anElement1 : getOclTestCollection()) {
		    if (anElement1.getName().equals("john")) {
		        result1.add(anElement1);
		    }
		    // no else
		}
		
		return result1;
	}
	
	public Set<String> getOclTestFlatten() {
		java.util.ArrayList<String> result1;
		result1 = new java.util.ArrayList<String>();
		
		/* Iterator Collect: Iterate through all elements and collect them. Elements which are collections are flattened. */
		for (org.tuml.testocl.OclTestCollection anElement1 : getOclTestCollection()) {
		    result1.add(anElement1.getName());
		}
		
		return tudresden.ocl20.pivot.tools.codegen.ocl2java.types.util.OclBags.asSet(result1);
	}
	
	public String getProperty1() {
		TinkerSet<String> tmp = this.property1;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
	}
	
	/** GetQualifiers is called from the collection in order to update the index used to implement the qualifier
	 * 
	 * @param tumlRuntimeProperty 
	 * @param node 
	 */
	@Override
	public List<Qualifier> getQualifiers(TumlRuntimeProperty tumlRuntimeProperty, TinkerNode node) {
		List<Qualifier> result = Collections.emptyList();
		OclTest1RuntimePropertyEnum runtimeProperty = OclTest1RuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result.isEmpty() ) {
			switch ( runtimeProperty ) {
				default:
					result = Collections.emptyList();
				break;
			}
		
		}
		return result;
	}
	
	/** GetSize is called from the collection in order to update the index used to implement a sequance's index
	 * 
	 * @param tumlRuntimeProperty 
	 */
	@Override
	public int getSize(TumlRuntimeProperty tumlRuntimeProperty) {
		int result = 0;
		OclTest1RuntimePropertyEnum runtimeProperty = OclTest1RuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result == 0 ) {
			switch ( runtimeProperty ) {
				case oclTestCollection:
					result = oclTestCollection.size();
				break;
			
				case property1:
					result = property1.size();
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
		this.property1 =  new TinkerSetImpl<String>(this, OclTest1RuntimePropertyEnum.property1);
		this.oclTestCollection =  new TinkerSetImpl<OclTestCollection>(this, OclTest1RuntimePropertyEnum.oclTestCollection);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (OclTest1RuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case oclTestCollection:
				this.oclTestCollection =  new TinkerSetImpl<OclTestCollection>(this, OclTest1RuntimePropertyEnum.oclTestCollection);
			break;
		
			case property1:
				this.property1 =  new TinkerSetImpl<String>(this, OclTest1RuntimePropertyEnum.property1);
			break;
		
		}
	}
	
	@Override
	public boolean isTinkerRoot() {
		return true;
	}
	
	public void removeFromOclTestCollection(OclTestCollection oclTestCollection) {
		if ( oclTestCollection != null ) {
			this.oclTestCollection.remove(oclTestCollection);
		}
	}
	
	public void removeFromOclTestCollection(Set<OclTestCollection> oclTestCollection) {
		if ( !oclTestCollection.isEmpty() ) {
			this.oclTestCollection.removeAll(oclTestCollection);
		}
	}
	
	public void removeFromProperty1(Set<String> property1) {
		if ( !property1.isEmpty() ) {
			this.property1.removeAll(property1);
		}
	}
	
	public void removeFromProperty1(String property1) {
		if ( property1 != null ) {
			this.property1.remove(property1);
		}
	}
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}
	
	public void setOclTestCollection(Set<OclTestCollection> oclTestCollection) {
		clearOclTestCollection();
		addToOclTestCollection(oclTestCollection);
	}
	
	public void setProperty1(String property1) {
		clearProperty1();
		addToProperty1(property1);
	}

	public enum OclTest1RuntimePropertyEnum implements TumlRuntimeProperty {
		property1(true,true,false,"testoclmodel__org__tuml__testocl__OclTest1__property1",false,false,true,false,1,1,false,false,false,false,true),
		oclTestCollection(false,true,true,"A_<oclTest1>_<oclTestCollection>",false,true,false,false,-1,1,false,false,false,false,true);
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
		/** Constructor for OclTest1RuntimePropertyEnum
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
		private OclTest1RuntimePropertyEnum(boolean onePrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower, boolean qualified, boolean inverseQualified, boolean ordered, boolean inverseOrdered, boolean unique) {
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
	
		static public OclTest1RuntimePropertyEnum fromLabel(String label) {
			if ( property1.getLabel().equals(label) ) {
				return property1;
			}
			if ( oclTestCollection.getLabel().equals(label) ) {
				return oclTestCollection;
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