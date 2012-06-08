package org.tinker.collectiontest;

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

public class Nightmare extends BaseTinker implements CompositionNode {
	private TinkerSet<String> name;
	private TinkerSet<String> nameNonUnique;
	private TinkerSet<God> god;
	private TinkerSet<God> godOfMemory;

	/** Constructor for Nightmare
	 * 
	 * @param compositeOwner 
	 */
	public Nightmare(God compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		initialiseProperties();
		createComponents();
		init(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/** Constructor for Nightmare
	 * 
	 * @param vertex 
	 */
	public Nightmare(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/** Default constructor for Nightmare
	 */
	public Nightmare() {
	}
	
	/** Constructor for Nightmare
	 * 
	 * @param persistent 
	 */
	public Nightmare(Boolean persistent) {
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
	
	public void addToGodOfMemory(God godOfMemory) {
		if ( godOfMemory != null ) {
			this.godOfMemory.add(godOfMemory);
		}
	}
	
	public void addToName(String name) {
		if ( name != null ) {
			this.name.add(name);
		}
	}
	
	public void addToNameNonUnique(String nameNonUnique) {
		if ( nameNonUnique != null ) {
			this.nameNonUnique.add(nameNonUnique);
		}
	}
	
	public void clearGod() {
		this.god.clear();
	}
	
	public void clearGodOfMemory() {
		this.godOfMemory.clear();
	}
	
	public void clearName() {
		this.name.clear();
	}
	
	public void clearNameNonUnique() {
		this.nameNonUnique.clear();
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
	
	public God getGodOfMemory() {
		TinkerSet<God> tmp = this.godOfMemory;
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
	
	public String getNameNonUnique() {
		TinkerSet<String> tmp = this.nameNonUnique;
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
		this.name =  new TinkerSetImpl<String>(this, NightmareRuntimePropertyEnum.name);
		this.nameNonUnique =  new TinkerSetImpl<String>(this, NightmareRuntimePropertyEnum.nameNonUnique);
		this.godOfMemory =  new TinkerSetImpl<God>(this, NightmareRuntimePropertyEnum.godOfMemory);
		this.god =  new TinkerSetImpl<God>(this, NightmareRuntimePropertyEnum.god);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (NightmareRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case god:
				this.god =  new TinkerSetImpl<God>(this, NightmareRuntimePropertyEnum.god);
			break;
		
			case godOfMemory:
				this.godOfMemory =  new TinkerSetImpl<God>(this, NightmareRuntimePropertyEnum.godOfMemory);
			break;
		
			case nameNonUnique:
				this.nameNonUnique =  new TinkerSetImpl<String>(this, NightmareRuntimePropertyEnum.nameNonUnique);
			break;
		
			case name:
				this.name =  new TinkerSetImpl<String>(this, NightmareRuntimePropertyEnum.name);
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
	
	public void removeFromGodOfMemory(God godOfMemory) {
		if ( godOfMemory != null ) {
			this.godOfMemory.remove(godOfMemory);
		}
	}
	
	public void removeFromGodOfMemory(Set<God> godOfMemory) {
		if ( !godOfMemory.isEmpty() ) {
			this.godOfMemory.removeAll(godOfMemory);
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
	
	public void removeFromNameNonUnique(Set<String> nameNonUnique) {
		if ( !nameNonUnique.isEmpty() ) {
			this.nameNonUnique.removeAll(nameNonUnique);
		}
	}
	
	public void removeFromNameNonUnique(String nameNonUnique) {
		if ( nameNonUnique != null ) {
			this.nameNonUnique.remove(nameNonUnique);
		}
	}
	
	public void setGod(God god) {
		clearGod();
		addToGod(god);
	}
	
	public void setGodOfMemory(God godOfMemory) {
		clearGodOfMemory();
		addToGodOfMemory(godOfMemory);
	}
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}
	
	public void setName(String name) {
		clearName();
		addToName(name);
	}
	
	public void setNameNonUnique(String nameNonUnique) {
		clearNameNonUnique();
		addToNameNonUnique(nameNonUnique);
	}

	public enum NightmareRuntimePropertyEnum implements TumlRuntimeProperty {
		name(true,true,false,"org__tinker__collectiontest__Nightmare__name",false,false,true,false,1,1),
		nameNonUnique(true,true,false,"org__tinker__collectiontest__Nightmare__nameNonUnique",false,false,true,false,1,1),
		godOfMemory(false,false,false,"A_<god>_<nightmare>_2",false,false,true,false,1,1),
		god(false,false,false,"A_<god>_<nightmare>",false,false,true,false,1,1);
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
		/** Constructor for NightmareRuntimePropertyEnum
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
		private NightmareRuntimePropertyEnum(boolean onePrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower) {
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
	
		static public NightmareRuntimePropertyEnum fromLabel(String label) {
			if ( name.getLabel().equals(label) ) {
				return name;
			}
			if ( nameNonUnique.getLabel().equals(label) ) {
				return nameNonUnique;
			}
			if ( godOfMemory.getLabel().equals(label) ) {
				return godOfMemory;
			}
			if ( god.getLabel().equals(label) ) {
				return god;
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