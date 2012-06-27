package org.tuml.testocl;

import com.tinkerpop.blueprints.Vertex;

import java.util.Set;
import java.util.UUID;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TinkerSetImpl;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.CompositionNode;
import org.tuml.runtime.domain.TinkerNode;

public class OclTestCollection extends BaseTinker implements CompositionNode {
	private TinkerSet<String> name;
	private TinkerSet<OclTest1> oclTest1;

	/** Constructor for OclTestCollection
	 * 
	 * @param compositeOwner 
	 */
	public OclTestCollection(OclTest1 compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		initialiseProperties();
		createComponents();
		init(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/** Constructor for OclTestCollection
	 * 
	 * @param vertex 
	 */
	public OclTestCollection(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/** Default constructor for OclTestCollection
	 */
	public OclTestCollection() {
	}
	
	/** Constructor for OclTestCollection
	 * 
	 * @param persistent 
	 */
	public OclTestCollection(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
		initialiseProperties();
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
	
	public void clearName() {
		this.name.clear();
	}
	
	public void clearOclTest1() {
		this.oclTest1.clear();
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
	
	@Override
	public TinkerNode getOwningObject() {
		return getOclTest1();
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
	
	/** This gets called on creation with the compositional owner. The composition owner does not itself need to be a composite node
	 * 
	 * @param compositeOwner 
	 */
	@Override
	public void init(TinkerNode compositeOwner) {
		this.addToOclTest1((OclTest1)compositeOwner);
		this.hasInitBeenCalled = true;
		initVariables();
	}
	
	public void initVariables() {
	}
	
	@Override
	public void initialiseProperties() {
		this.oclTest1 =  new TinkerSetImpl<OclTest1>(this, OclTestCollectionRuntimePropertyEnum.oclTest1);
		this.name =  new TinkerSetImpl<String>(this, OclTestCollectionRuntimePropertyEnum.name);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (OclTestCollectionRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case name:
				this.name =  new TinkerSetImpl<String>(this, OclTestCollectionRuntimePropertyEnum.name);
			break;
		
			case oclTest1:
				this.oclTest1 =  new TinkerSetImpl<OclTest1>(this, OclTestCollectionRuntimePropertyEnum.oclTest1);
			break;
		
		}
	}
	
	@Override
	public boolean isTinkerRoot() {
		return false;
	}
	
	public void removeFromName(Set<String> name) {
		if ( !name.isEmpty() ) {
			this.name.removeAll(name);
		}
	}
	
	public void removeFromName(String name) {
		if ( name != null ) {
			this.name.remove(name);
		}
	}
	
	public void removeFromOclTest1(OclTest1 oclTest1) {
		if ( oclTest1 != null ) {
			this.oclTest1.remove(oclTest1);
		}
	}
	
	public void removeFromOclTest1(Set<OclTest1> oclTest1) {
		if ( !oclTest1.isEmpty() ) {
			this.oclTest1.removeAll(oclTest1);
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

	public enum OclTestCollectionRuntimePropertyEnum implements TumlRuntimeProperty {
		oclTest1(false,false,false,"A_<oclTest1>_<oclTestCollection>",false,false,true,false,1,1),
		name(true,true,false,"testoclmodel__org__tuml__testocl__OclTestCollection__name",false,false,true,false,1,1);
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
		/** Constructor for OclTestCollectionRuntimePropertyEnum
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
		private OclTestCollectionRuntimePropertyEnum(boolean onePrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower) {
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
	
		static public OclTestCollectionRuntimePropertyEnum fromLabel(String label) {
			if ( oclTest1.getLabel().equals(label) ) {
				return oclTest1;
			}
			if ( name.getLabel().equals(label) ) {
				return name;
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