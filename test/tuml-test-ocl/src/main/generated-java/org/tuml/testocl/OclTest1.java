package org.tuml.testocl;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

import java.util.Set;
import java.util.UUID;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TinkerSetImpl;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.TinkerNode;

public class OclTest1 extends BaseTinker implements TinkerNode {
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
	
	public TinkerSet<OclTestCollection> getOclTestCollection() {
		return this.oclTestCollection;
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
	
	public String getProperty1() {
		TinkerSet<String> tmp = this.property1;
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
		this.oclTestCollection =  new TinkerSetImpl<OclTestCollection>(this, OclTest1RuntimePropertyEnum.oclTestCollection);
		this.property1 =  new TinkerSetImpl<String>(this, OclTest1RuntimePropertyEnum.property1);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (OclTest1RuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case property1:
				this.property1 =  new TinkerSetImpl<String>(this, OclTest1RuntimePropertyEnum.property1);
			break;
		
			case oclTestCollection:
				this.oclTestCollection =  new TinkerSetImpl<OclTestCollection>(this, OclTest1RuntimePropertyEnum.oclTestCollection);
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
		oclTestCollection(false,true,true,"A_<oclTest1>_<oclTestCollection>",false,true,false,false,-1,1),
		property1(true,true,false,"testoclmodel__org__tuml__testocl__OclTest1__property1",false,false,true,false,1,1);
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
		 */
		private OclTest1RuntimePropertyEnum(boolean onePrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower) {
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
		}
	
		static public OclTest1RuntimePropertyEnum fromLabel(String label) {
			if ( oclTestCollection.getLabel().equals(label) ) {
				return oclTestCollection;
			}
			if ( property1.getLabel().equals(label) ) {
				return property1;
			}
			throw new IllegalStateException();
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
		
		@Override
		public boolean isValid(int elementCount) {
			return (getUpper() == -1 || elementCount <= getUpper()) && elementCount >= getLower();
		}
	
	}
}