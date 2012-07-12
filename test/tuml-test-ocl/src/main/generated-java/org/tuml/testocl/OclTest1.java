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
import org.tuml.runtime.collection.ocl.BooleanVisitor;
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
		return getOclTestCollection()->any(temp1 : OclTestCollection | getName().<>('john'));
	}
	
	public TinkerSet<OclTestCollection> getOclTestCollection() {
		return this.oclTestCollection;
	}
	
	public Set<OclTestCollection2> getOclTestCollection2() {
		return getOclTestCollection()->collect(temp1 : OclTestCollection | getOclTestCollection2())->asSet();
	}
	
	public Collection<String> getOclTestCollection2Name() {
		return getOclTestCollection()->collect(temp1 : OclTestCollection | getOclTestCollection2())->collect(temp2 : OclTestCollection2 | getName());
	}
	
	public Set<OclTestCollection> getOclTestCollectionSelect() {
		return getOclTestCollection().select(new BooleanVisitor<OclTestCollection>() {
			@Override
			public boolean evaluate(OclTestCollection e) {
				return e.getName().equals("john");
			}
		});
//		return getOclTestCollection()->select(temp1 : OclTestCollection | getName().=('john'));
	}
	
	public Set<String> getOclTestFlatten() {
		return getOclTestCollection()->collect(temp1 : OclTestCollection | getName())->asSet();
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
				case property1:
					result = property1.size();
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
		oclTestCollection(false,true,true,"A_<oclTest1>_<oclTestCollection>",false,true,false,false,-1,1,false,false,false,false,true),
		property1(true,true,false,"testoclmodel__org__tuml__testocl__OclTest1__property1",false,false,true,false,1,1,false,false,false,false,true);
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
			if ( oclTestCollection.getLabel().equals(label) ) {
				return oclTestCollection;
			}
			if ( property1.getLabel().equals(label) ) {
				return property1;
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