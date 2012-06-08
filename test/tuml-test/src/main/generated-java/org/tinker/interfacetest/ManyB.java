package org.tinker.interfacetest;

import com.tinkerpop.blueprints.Vertex;

import java.util.Set;
import java.util.UUID;

import org.tinker.concretetest.God;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TinkerSetImpl;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.CompositionNode;
import org.tuml.runtime.domain.TinkerNode;

public class ManyB extends BaseTinker implements CompositionNode, IManyB {
	private TinkerSet<God> god;
	private TinkerSet<String> name;
	private TinkerSet<IManyA> iManyA;

	/** Constructor for ManyB
	 * 
	 * @param compositeOwner 
	 */
	public ManyB(God compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		initialiseProperties();
		createComponents();
		init(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/** Constructor for ManyB
	 * 
	 * @param vertex 
	 */
	public ManyB(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/** Default constructor for ManyB
	 */
	public ManyB() {
	}
	
	/** Constructor for ManyB
	 * 
	 * @param persistent 
	 */
	public ManyB(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
		initialiseProperties();
		createComponents();
	}

	public void addToGod(God god) {
		if ( god != null ) {
			this.god.add(god);
		}
	}
	
	public void addToIManyA(IManyA iManyA) {
		if ( iManyA != null ) {
			this.iManyA.add(iManyA);
		}
	}
	
	public void addToIManyA(Set<IManyA> iManyA) {
		if ( !iManyA.isEmpty() ) {
			this.iManyA.addAll(iManyA);
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
	
	public void clearIManyA() {
		this.iManyA.clear();
	}
	
	public void clearName() {
		this.name.clear();
	}
	
	public void createComponents() {
	}
	
	@Override
	public void delete() {
		GraphDb.getDb().removeVertex(this.vertex);
	}
	
	public God getGod() {
		TinkerSet<God> tmp = this.god;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
	}
	
	public TinkerSet<IManyA> getIManyA() {
		return this.iManyA;
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
	
	@Override
	public TinkerNode getOwningObject() {
		return getGod();
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
		this.addToGod((God)compositeOwner);
		this.hasInitBeenCalled = true;
		initVariables();
	}
	
	public void initVariables() {
	}
	
	@Override
	public void initialiseProperties() {
		this.god =  new TinkerSetImpl<God>(this, ManyBRuntimePropertyEnum.god);
		this.name =  new TinkerSetImpl<String>(this, ManyBRuntimePropertyEnum.name);
		this.iManyA =  new TinkerSetImpl<IManyA>(this, ManyBRuntimePropertyEnum.iManyA);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (ManyBRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case iManyA:
				this.iManyA =  new TinkerSetImpl<IManyA>(this, ManyBRuntimePropertyEnum.iManyA);
			break;
		
			case name:
				this.name =  new TinkerSetImpl<String>(this, ManyBRuntimePropertyEnum.name);
			break;
		
			case god:
				this.god =  new TinkerSetImpl<God>(this, ManyBRuntimePropertyEnum.god);
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
	
	public void removeFromGod(Set<God> god) {
		if ( !god.isEmpty() ) {
			this.god.removeAll(god);
		}
	}
	
	public void removeFromIManyA(IManyA iManyA) {
		if ( iManyA != null ) {
			this.iManyA.remove(iManyA);
		}
	}
	
	public void removeFromIManyA(Set<IManyA> iManyA) {
		if ( !iManyA.isEmpty() ) {
			this.iManyA.removeAll(iManyA);
		}
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
	
	public void setGod(God god) {
		clearGod();
		addToGod(god);
	}
	
	public void setIManyA(Set<IManyA> iManyA) {
		clearIManyA();
		addToIManyA(iManyA);
	}
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}
	
	public void setName(String name) {
		clearName();
		addToName(name);
	}

	public enum ManyBRuntimePropertyEnum implements TumlRuntimeProperty {
		god(false,false,false,"A_<god>_<iMany>",false,false,true,false,1,1),
		name(true,true,false,"org__tinker__interfacetest__IMany__name",false,false,true,false,1,1),
		iManyA(false,true,false,"A_<iManyA>_<iManyB>",false,false,false,true,-1,0);
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
		/** Constructor for ManyBRuntimePropertyEnum
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
		private ManyBRuntimePropertyEnum(boolean onePrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower) {
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
	
		static public ManyBRuntimePropertyEnum fromLabel(String label) {
			if ( god.getLabel().equals(label) ) {
				return god;
			}
			if ( name.getLabel().equals(label) ) {
				return name;
			}
			if ( iManyA.getLabel().equals(label) ) {
				return iManyA;
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