package org.tuml.testocl;

import com.tinkerpop.blueprints.Vertex;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

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

public class OclTestCollection extends BaseTuml implements CompositionNode {
	static final public long serialVersionUID = 1L;
	private TinkerSet<String> name;
	private TinkerSet<OclTestCollection2> oclTestCollection2;
	private TinkerSet<OclTest1> oclTest1;
	private TinkerSet<OclTest2> oclTest2;

	/**
	 * constructor for OclTestCollection
	 * 
	 * @param compositeOwner 
	 */
	public OclTestCollection(OclTest1 compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		initialiseProperties();
		initVariables();
		createComponents();
		addToOclTest1(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/**
	 * constructor for OclTestCollection
	 * 
	 * @param vertex 
	 */
	public OclTestCollection(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/**
	 * default constructor for OclTestCollection
	 */
	public OclTestCollection() {
	}
	
	/**
	 * constructor for OclTestCollection
	 * 
	 * @param persistent 
	 */
	public OclTestCollection(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
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
	
	public void addToOclTest1(OclTest1 oclTest1) {
		if ( oclTest1 != null ) {
			this.oclTest1.add(oclTest1);
		}
	}
	
	public void addToOclTest2(OclTest2 oclTest2) {
		if ( oclTest2 != null ) {
			this.oclTest2.add(oclTest2);
		}
	}
	
	public void addToOclTestCollection2(OclTestCollection2 oclTestCollection2) {
		if ( oclTestCollection2 != null ) {
			this.oclTestCollection2.add(oclTestCollection2);
		}
	}
	
	public void addToOclTestCollection2(TinkerSet<OclTestCollection2> oclTestCollection2) {
		if ( !oclTestCollection2.isEmpty() ) {
			this.oclTestCollection2.addAll(oclTestCollection2);
		}
	}
	
	public void clearName() {
		this.name.clear();
	}
	
	public void clearOclTest1() {
		this.oclTest1.clear();
	}
	
	public void clearOclTest2() {
		this.oclTest2.clear();
	}
	
	public void clearOclTestCollection2() {
		this.oclTestCollection2.clear();
	}
	
	public void createComponents() {
	}
	
	@Override
	public void delete() {
		for ( OclTestCollection2 child : getOclTestCollection2() ) {
			child.delete();
		}
		GraphDb.getDb().removeVertex(this.vertex);
	}
	
	/**
	 * Implements the ocl statement for derived property 'derivedTest'
	 * <pre>
	 * package testoclmodel::org::tuml::testocl
	 *     context OclTestCollection::derivedTest : OclTest2
	 *     derive: self.oclTest2
	 * endpackage
	 * </pre>
	 */
	public OclTest2 getDerivedTest() {
		return getOclTest2();
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
	
	public OclTest1 getOclTest1() {
		TinkerSet<OclTest1> tmp = this.oclTest1;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
	}
	
	public OclTest2 getOclTest2() {
		TinkerSet<OclTest2> tmp = this.oclTest2;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
	}
	
	public TinkerSet<OclTestCollection2> getOclTestCollection2() {
		return this.oclTestCollection2;
	}
	
	@Override
	public TumlNode getOwningObject() {
		return getOclTest1();
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
		OclTestCollectionRuntimePropertyEnum runtimeProperty = OclTestCollectionRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
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
		OclTestCollectionRuntimePropertyEnum runtimeProperty = OclTestCollectionRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result == 0 ) {
			switch ( runtimeProperty ) {
				case oclTest2:
					result = oclTest2.size();
				break;
			
				case oclTestCollection2:
					result = oclTestCollection2.size();
				break;
			
				case oclTest1:
					result = oclTest1.size();
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
		this.name =  new TinkerSetImpl<String>(this, OclTestCollectionRuntimePropertyEnum.name);
		this.oclTest1 =  new TinkerSetImpl<OclTest1>(this, OclTestCollectionRuntimePropertyEnum.oclTest1);
		this.oclTestCollection2 =  new TinkerSetImpl<OclTestCollection2>(this, OclTestCollectionRuntimePropertyEnum.oclTestCollection2);
		this.oclTest2 =  new TinkerSetImpl<OclTest2>(this, OclTestCollectionRuntimePropertyEnum.oclTest2);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (OclTestCollectionRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case oclTest2:
				this.oclTest2 =  new TinkerSetImpl<OclTest2>(this, OclTestCollectionRuntimePropertyEnum.oclTest2);
			break;
		
			case oclTestCollection2:
				this.oclTestCollection2 =  new TinkerSetImpl<OclTestCollection2>(this, OclTestCollectionRuntimePropertyEnum.oclTestCollection2);
			break;
		
			case oclTest1:
				this.oclTest1 =  new TinkerSetImpl<OclTest1>(this, OclTestCollectionRuntimePropertyEnum.oclTest1);
			break;
		
			case name:
				this.name =  new TinkerSetImpl<String>(this, OclTestCollectionRuntimePropertyEnum.name);
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
	
	public void removeFromOclTest1(OclTest1 oclTest1) {
		if ( oclTest1 != null ) {
			this.oclTest1.remove(oclTest1);
		}
	}
	
	public void removeFromOclTest1(TinkerSet<OclTest1> oclTest1) {
		if ( !oclTest1.isEmpty() ) {
			this.oclTest1.removeAll(oclTest1);
		}
	}
	
	public void removeFromOclTest2(OclTest2 oclTest2) {
		if ( oclTest2 != null ) {
			this.oclTest2.remove(oclTest2);
		}
	}
	
	public void removeFromOclTest2(TinkerSet<OclTest2> oclTest2) {
		if ( !oclTest2.isEmpty() ) {
			this.oclTest2.removeAll(oclTest2);
		}
	}
	
	public void removeFromOclTestCollection2(OclTestCollection2 oclTestCollection2) {
		if ( oclTestCollection2 != null ) {
			this.oclTestCollection2.remove(oclTestCollection2);
		}
	}
	
	public void removeFromOclTestCollection2(TinkerSet<OclTestCollection2> oclTestCollection2) {
		if ( !oclTestCollection2.isEmpty() ) {
			this.oclTestCollection2.removeAll(oclTestCollection2);
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
	
	public void setOclTest1(OclTest1 oclTest1) {
		clearOclTest1();
		addToOclTest1(oclTest1);
	}
	
	public void setOclTest2(OclTest2 oclTest2) {
		clearOclTest2();
		addToOclTest2(oclTest2);
	}
	
	public void setOclTestCollection2(TinkerSet<OclTestCollection2> oclTestCollection2) {
		clearOclTestCollection2();
		addToOclTestCollection2(oclTestCollection2);
	}

	public enum OclTestCollectionRuntimePropertyEnum implements TumlRuntimeProperty {
		name(true,true,false,"testoclmodel__org__tuml__testocl__OclTestCollection__name",false,false,true,false,1,1,false,false,false,false,true),
		oclTest1(false,false,false,"A_<oclTest1>_<oclTestCollection>",false,false,true,false,1,1,false,false,false,false,true),
		oclTestCollection2(false,true,true,"A_<oclTestCollection>_<oclTestCollection2>",false,true,false,false,-1,0,false,false,false,false,true),
		oclTest2(false,false,false,"A_<oclTest2>_<oclTestCollection>",false,false,true,false,1,1,false,false,false,false,true);
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
		 * constructor for OclTestCollectionRuntimePropertyEnum
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
		private OclTestCollectionRuntimePropertyEnum(boolean onePrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower, boolean qualified, boolean inverseQualified, boolean ordered, boolean inverseOrdered, boolean unique) {
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
	
		static public OclTestCollectionRuntimePropertyEnum fromLabel(String label) {
			if ( name.getLabel().equals(label) ) {
				return name;
			}
			if ( oclTest1.getLabel().equals(label) ) {
				return oclTest1;
			}
			if ( oclTestCollection2.getLabel().equals(label) ) {
				return oclTestCollection2;
			}
			if ( oclTest2.getLabel().equals(label) ) {
				return oclTest2;
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