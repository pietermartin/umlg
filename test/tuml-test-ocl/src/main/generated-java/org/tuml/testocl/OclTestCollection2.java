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

public class OclTestCollection2 extends BaseTinker implements CompositionNode {
	private TinkerSet<String> name;
	private TinkerSet<OclTestCollection> oclTestCollection;

	/** Constructor for OclTestCollection2
	 * 
	 * @param compositeOwner 
	 */
	public OclTestCollection2(OclTestCollection compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		initialiseProperties();
		createComponents();
		init(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/** Constructor for OclTestCollection2
	 * 
	 * @param vertex 
	 */
	public OclTestCollection2(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/** Default constructor for OclTestCollection2
	 */
	public OclTestCollection2() {
	}
	
	/** Constructor for OclTestCollection2
	 * 
	 * @param persistent 
	 */
	public OclTestCollection2(Boolean persistent) {
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
	
	public void addToOclTestCollection(OclTestCollection oclTestCollection) {
		if ( oclTestCollection != null ) {
			this.oclTestCollection.add(oclTestCollection);
		}
	}
	
	public void clearName() {
		this.name.clear();
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
	
	@Override
	public int getObjectVersion() {
		return TinkerIdUtilFactory.getIdUtil().getVersion(this.vertex);
	}
	
	public OclTestCollection getOclTestCollection() {
		TinkerSet<OclTestCollection> tmp = this.oclTestCollection;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
	}
	
	@Override
	public TinkerNode getOwningObject() {
		return getOclTestCollection();
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
		this.addToOclTestCollection((OclTestCollection)compositeOwner);
		this.hasInitBeenCalled = true;
		initVariables();
	}
	
	public void initVariables() {
	}
	
	@Override
	public void initialiseProperties() {
		this.oclTestCollection =  new TinkerSetImpl<OclTestCollection>(this, OclTestCollection2RuntimePropertyEnum.oclTestCollection);
		this.name =  new TinkerSetImpl<String>(this, OclTestCollection2RuntimePropertyEnum.name);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (OclTestCollection2RuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case name:
				this.name =  new TinkerSetImpl<String>(this, OclTestCollection2RuntimePropertyEnum.name);
			break;
		
			case oclTestCollection:
				this.oclTestCollection =  new TinkerSetImpl<OclTestCollection>(this, OclTestCollection2RuntimePropertyEnum.oclTestCollection);
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
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}
	
	public void setName(String name) {
		clearName();
		addToName(name);
	}
	
	public void setOclTestCollection(OclTestCollection oclTestCollection) {
		clearOclTestCollection();
		addToOclTestCollection(oclTestCollection);
	}

	public enum OclTestCollection2RuntimePropertyEnum implements TumlRuntimeProperty {
		oclTestCollection(false,false,false,"A_<oclTestCollection>_<oclTestCollection2>",false,false,true,false,1,1),
		name(true,true,false,"testoclmodel__org__tuml__testocl__OclTestCollection2__name",false,false,true,false,1,1);
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
		/** Constructor for OclTestCollection2RuntimePropertyEnum
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
		private OclTestCollection2RuntimePropertyEnum(boolean onePrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower) {
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
	
		static public OclTestCollection2RuntimePropertyEnum fromLabel(String label) {
			if ( oclTestCollection.getLabel().equals(label) ) {
				return oclTestCollection;
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