package org.tuml.qualifier;

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

public class Nature extends BaseTinker implements CompositionNode {
	private TinkerSet<String> natureName;
	private TinkerSet<God> god;

	/** Constructor for Nature
	 * 
	 * @param compositeOwner 
	 */
	public Nature(God compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		initialiseProperties();
		createComponents();
		init(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/** Constructor for Nature
	 * 
	 * @param vertex 
	 */
	public Nature(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/** Default constructor for Nature
	 */
	public Nature() {
	}
	
	/** Constructor for Nature
	 * 
	 * @param persistent 
	 */
	public Nature(Boolean persistent) {
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
	
	public void addToNatureName(String natureName) {
		if ( natureName != null ) {
			this.natureName.add(natureName);
		}
	}
	
	public void clearGod() {
		this.god.clear();
	}
	
	public void clearNatureName() {
		this.natureName.clear();
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
	
	@Override
	public Long getId() {
		return TinkerIdUtilFactory.getIdUtil().getId(this.vertex);
	}
	
	public String getNatureName() {
		TinkerSet<String> tmp = this.natureName;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
	}
	
	public String getNatureQualifier1() {
		return getNatureName();
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
		this.god =  new TinkerSetImpl<God>(this, NatureRuntimePropertyEnum.god);
		this.natureName =  new TinkerSetImpl<String>(this, NatureRuntimePropertyEnum.natureName);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (NatureRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case natureName:
				this.natureName =  new TinkerSetImpl<String>(this, NatureRuntimePropertyEnum.natureName);
			break;
		
			case god:
				this.god =  new TinkerSetImpl<God>(this, NatureRuntimePropertyEnum.god);
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
	
	public void removeFromNatureName(Set<String> natureName) {
		if ( !natureName.isEmpty() ) {
			this.natureName.removeAll(natureName);
		}
	}
	
	public void removeFromNatureName(String natureName) {
		if ( natureName != null ) {
			this.natureName.remove(natureName);
		}
	}
	
	public void setGod(God god) {
		clearGod();
		addToGod(god);
	}
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}
	
	public void setNatureName(String natureName) {
		clearNatureName();
		addToNatureName(natureName);
	}

	public enum NatureRuntimePropertyEnum implements TumlRuntimeProperty {
		god(false,false,false,"A_<god>_<nature>",false,false,true,false,1,1),
		natureName(true,true,false,"tuml-test-basic-model__org__tuml__qualifier__Nature__natureName",false,false,true,false,1,1);
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
		/** Constructor for NatureRuntimePropertyEnum
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
		private NatureRuntimePropertyEnum(boolean onePrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower) {
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
	
		static public NatureRuntimePropertyEnum fromLabel(String label) {
			if ( god.getLabel().equals(label) ) {
				return god;
			}
			if ( natureName.getLabel().equals(label) ) {
				return natureName;
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