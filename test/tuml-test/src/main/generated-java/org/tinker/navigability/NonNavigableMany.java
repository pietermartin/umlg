package org.tinker.navigability;

import com.tinkerpop.blueprints.Vertex;

import java.util.Set;
import java.util.UUID;

import org.tinker.concretetest.God;
import org.tinker.concretetest.Universe;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TinkerSetImpl;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.CompositionNode;
import org.tuml.runtime.domain.TinkerNode;

public class NonNavigableMany extends BaseTinker implements CompositionNode {
	private TinkerSet<String> name;
	private TinkerSet<Universe> universe;
	private TinkerSet<God> god;

	/** Constructor for NonNavigableMany
	 * 
	 * @param compositeOwner 
	 */
	public NonNavigableMany(God compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		initialiseProperties();
		createComponents();
		init(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/** Constructor for NonNavigableMany
	 * 
	 * @param vertex 
	 */
	public NonNavigableMany(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/** Default constructor for NonNavigableMany
	 */
	public NonNavigableMany() {
	}
	
	/** Constructor for NonNavigableMany
	 * 
	 * @param persistent 
	 */
	public NonNavigableMany(Boolean persistent) {
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
		this.addToGod((God)compositeOwner);
		this.hasInitBeenCalled = true;
		initVariables();
	}
	
	public void initVariables() {
	}
	
	@Override
	public void initialiseProperties() {
		this.god =  new TinkerSetImpl<God>(this, NonNavigableManyRuntimePropertyEnum.god);
		this.universe =  new TinkerSetImpl<Universe>(this, NonNavigableManyRuntimePropertyEnum.universe);
		this.name =  new TinkerSetImpl<String>(this, NonNavigableManyRuntimePropertyEnum.name);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (NonNavigableManyRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case name:
				this.name =  new TinkerSetImpl<String>(this, NonNavigableManyRuntimePropertyEnum.name);
			break;
		
			case universe:
				this.universe =  new TinkerSetImpl<Universe>(this, NonNavigableManyRuntimePropertyEnum.universe);
			break;
		
			case god:
				this.god =  new TinkerSetImpl<God>(this, NonNavigableManyRuntimePropertyEnum.god);
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

	public enum NonNavigableManyRuntimePropertyEnum implements TumlRuntimeProperty {
		god(false,false,false,"A_<god>_<nonNavigableMany>",false,false,true,false,1,1),
		universe(false,false,false,"A_<universe>_<nonNavigableMany>",false,false,true,false,1,1),
		name(true,true,false,"org__tinker__navigability__NonNavigableMany__name",false,false,true,false,1,1);
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
		/** Constructor for NonNavigableManyRuntimePropertyEnum
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
		private NonNavigableManyRuntimePropertyEnum(boolean onePrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower) {
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
	
		static public NonNavigableManyRuntimePropertyEnum fromLabel(String label) {
			if ( god.getLabel().equals(label) ) {
				return god;
			}
			if ( universe.getLabel().equals(label) ) {
				return universe;
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