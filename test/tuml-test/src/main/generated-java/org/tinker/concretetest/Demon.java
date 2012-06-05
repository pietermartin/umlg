package org.tinker.concretetest;

import com.tinkerpop.blueprints.pgm.Vertex;

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

public class Demon extends BaseTinker implements CompositionNode {
	private TinkerSet<String> name;
	private TinkerSet<God> god;
	private TinkerSet<Universe> universe;

	/** Constructor for Demon
	 * 
	 * @param compositeOwner 
	 */
	public Demon(God compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		createComponents();
		initialiseProperties();
		init(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/** Constructor for Demon
	 * 
	 * @param vertex 
	 */
	public Demon(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/** Default constructor for Demon
	 */
	public Demon() {
	}
	
	/** Constructor for Demon
	 * 
	 * @param persistent 
	 */
	public Demon(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
		initialiseProperties();
	}

	public void addToGod(God god) {
		if ( god != null ) {
			this.god.add(god);
		}
	}
	
	public void addToName(String name) {
		if ( name != null ) {
			this.name.add(name);
		}
	}
	
	public void addToUniverse(Universe universe) {
		if ( universe != null ) {
			this.universe.add(universe);
		}
	}
	
	public void clearGod() {
		this.god.clear();
	}
	
	public void clearName() {
		this.name.clear();
	}
	
	public void clearUniverse() {
		this.universe.clear();
	}
	
	public void createComponents() {
	}
	
	@Override
	public void delete() {
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
	
	public Universe getUniverse() {
		TinkerSet<Universe> tmp = this.universe;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
	}
	
	/** This gets called on creation with the compositional owner. The composition owner does not itself need to be a composite node
	 * 
	 * @param compositeOwner 
	 */
	@Override
	public void init(TinkerNode compositeOwner) {
		this.god.add((God)compositeOwner);
		this.hasInitBeenCalled = true;
		initVariables();
	}
	
	public void initVariables() {
	}
	
	@Override
	public void initialiseProperties() {
		this.universe =  new TinkerSetImpl<Universe>(this, DemonRuntimePropertyEnum.UNIVERSE);
		this.name =  new TinkerSetImpl<String>(this, DemonRuntimePropertyEnum.NAME);
		this.god =  new TinkerSetImpl<God>(this, DemonRuntimePropertyEnum.GOD);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (DemonRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case GOD:
				this.god =  new TinkerSetImpl<God>(this, DemonRuntimePropertyEnum.GOD);
			break;
		
			case NAME:
				this.name =  new TinkerSetImpl<String>(this, DemonRuntimePropertyEnum.NAME);
			break;
		
			case UNIVERSE:
				this.universe =  new TinkerSetImpl<Universe>(this, DemonRuntimePropertyEnum.UNIVERSE);
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
	
	public void removeFromUniverse(Set<Universe> universe) {
		if ( !universe.isEmpty() ) {
			this.universe.removeAll(universe);
		}
	}
	
	public void removeFromUniverse(Universe universe) {
		if ( universe != null ) {
			this.universe.remove(universe);
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
	
	public void setName(String name) {
		clearName();
		addToName(name);
	}
	
	public void setUniverse(Universe universe) {
		clearUniverse();
		addToUniverse(universe);
	}

	public enum DemonRuntimePropertyEnum implements TumlRuntimeProperty {
		UNIVERSE(false,false,"A_<universe>_<demon>",false,false,true,false,1,1),
		NAME(true,false,"org__tinker__concretetest__Demon__name",false,false,true,false,1,1),
		GOD(false,false,"A_<god>_<demon>",false,false,true,false,1,1);
		private boolean controllingSide;
		private boolean composite;
		private String label;
		private boolean oneToOne;
		private boolean oneToMany;
		private boolean manyToOne;
		private boolean manyToMany;
		private int upper;
		private int lower;
		/** Constructor for DemonRuntimePropertyEnum
		 * 
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
		private DemonRuntimePropertyEnum(boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower) {
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
	
		static public DemonRuntimePropertyEnum fromLabel(String label) {
			if ( UNIVERSE.getLabel().equals(label) ) {
				return UNIVERSE;
			}
			if ( NAME.getLabel().equals(label) ) {
				return NAME;
			}
			if ( GOD.getLabel().equals(label) ) {
				return GOD;
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