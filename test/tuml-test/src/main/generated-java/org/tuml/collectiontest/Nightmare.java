package org.tuml.collectiontest;

import com.tinkerpop.blueprints.Vertex;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.tuml.concretetest.God;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.collection.Qualifier;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TinkerSetImpl;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.CompositionNode;
import org.tuml.runtime.domain.TinkerNode;

public class Nightmare extends BaseTinker implements CompositionNode {
	static final public long serialVersionUID = 1L;
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
		initVariables();
		createComponents();
		addToGod(compositeOwner);
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
		initVariables();
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
	
	public String getMemoryQualifier1() {
		return getNameNonUnique();
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
	
	public String getQualifier1() {
		return getName();
	}
	
	/** GetQualifiers is called from the collection in order to update the index used to implement the qualifier
	 * 
	 * @param tumlRuntimeProperty 
	 * @param node 
	 */
	@Override
	public List<Qualifier> getQualifiers(TumlRuntimeProperty tumlRuntimeProperty, TinkerNode node) {
		List<Qualifier> result = Collections.emptyList();
		NightmareRuntimePropertyEnum runtimeProperty = NightmareRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
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
		NightmareRuntimePropertyEnum runtimeProperty = NightmareRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result == 0 ) {
			switch ( runtimeProperty ) {
				case name:
					result = name.size();
				break;
			
				case god:
					result = god.size();
				break;
			
				case nameNonUnique:
					result = nameNonUnique.size();
				break;
			
				case godOfMemory:
					result = godOfMemory.size();
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
		this.godOfMemory =  new TinkerSetImpl<God>(this, NightmareRuntimePropertyEnum.godOfMemory);
		this.nameNonUnique =  new TinkerSetImpl<String>(this, NightmareRuntimePropertyEnum.nameNonUnique);
		this.god =  new TinkerSetImpl<God>(this, NightmareRuntimePropertyEnum.god);
		this.name =  new TinkerSetImpl<String>(this, NightmareRuntimePropertyEnum.name);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (NightmareRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case name:
				this.name =  new TinkerSetImpl<String>(this, NightmareRuntimePropertyEnum.name);
			break;
		
			case god:
				this.god =  new TinkerSetImpl<God>(this, NightmareRuntimePropertyEnum.god);
			break;
		
			case nameNonUnique:
				this.nameNonUnique =  new TinkerSetImpl<String>(this, NightmareRuntimePropertyEnum.nameNonUnique);
			break;
		
			case godOfMemory:
				this.godOfMemory =  new TinkerSetImpl<God>(this, NightmareRuntimePropertyEnum.godOfMemory);
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
		godOfMemory(false,false,false,"A_<god>_<nightmare>_2",false,false,true,false,1,1,false,true,false,false,true),
		nameNonUnique(true,true,false,"tuml-test__org__tuml__collectiontest__Nightmare__nameNonUnique",false,false,true,false,1,1,false,false,false,false,true),
		god(false,false,false,"A_<god>_<nightmare>",false,false,true,false,1,1,false,true,false,false,true),
		name(true,true,false,"tuml-test__org__tuml__collectiontest__Nightmare__name",false,false,true,false,1,1,false,false,false,false,true);
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
		 * @param qualified 
		 * @param inverseQualified 
		 * @param ordered 
		 * @param inverseOrdered 
		 * @param unique 
		 */
		private NightmareRuntimePropertyEnum(boolean onePrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower, boolean qualified, boolean inverseQualified, boolean ordered, boolean inverseOrdered, boolean unique) {
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
	
		static public NightmareRuntimePropertyEnum fromLabel(String label) {
			if ( godOfMemory.getLabel().equals(label) ) {
				return godOfMemory;
			}
			if ( nameNonUnique.getLabel().equals(label) ) {
				return nameNonUnique;
			}
			if ( god.getLabel().equals(label) ) {
				return god;
			}
			if ( name.getLabel().equals(label) ) {
				return name;
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