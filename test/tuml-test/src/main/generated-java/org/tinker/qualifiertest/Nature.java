package org.tinker.qualifiertest;

import com.tinkerpop.blueprints.pgm.Vertex;

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

public class Nature extends BaseTinker implements CompositionNode {
	private TinkerSet<String> name1;
	private TinkerSet<String> name2;
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
	
	public void addToName1(String name1) {
		if ( name1 != null ) {
			this.name1.add(name1);
		}
	}
	
	public void addToName2(String name2) {
		if ( name2 != null ) {
			this.name2.add(name2);
		}
	}
	
	public void clearGod() {
		this.god.clear();
	}
	
	public void clearName1() {
		this.name1.clear();
	}
	
	public void clearName2() {
		this.name2.clear();
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
	
	public String getName1() {
		TinkerSet<String> tmp = this.name1;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
	}
	
	public String getName2() {
		TinkerSet<String> tmp = this.name2;
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
		this.name1 =  new TinkerSetImpl<String>(this, NatureRuntimePropertyEnum.name1);
		this.god =  new TinkerSetImpl<God>(this, NatureRuntimePropertyEnum.god);
		this.name2 =  new TinkerSetImpl<String>(this, NatureRuntimePropertyEnum.name2);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (NatureRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case name2:
				this.name2 =  new TinkerSetImpl<String>(this, NatureRuntimePropertyEnum.name2);
			break;
		
			case god:
				this.god =  new TinkerSetImpl<God>(this, NatureRuntimePropertyEnum.god);
			break;
		
			case name1:
				this.name1 =  new TinkerSetImpl<String>(this, NatureRuntimePropertyEnum.name1);
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
	
	public void removeFromName1(Set<String> name1) {
		if ( !name1.isEmpty() ) {
			this.name1.removeAll(name1);
		}
	}
	
	public void removeFromName1(String name1) {
		if ( name1 != null ) {
			this.name1.remove(name1);
		}
	}
	
	public void removeFromName2(Set<String> name2) {
		if ( !name2.isEmpty() ) {
			this.name2.removeAll(name2);
		}
	}
	
	public void removeFromName2(String name2) {
		if ( name2 != null ) {
			this.name2.remove(name2);
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
	
	public void setName1(String name1) {
		clearName1();
		addToName1(name1);
	}
	
	public void setName2(String name2) {
		clearName2();
		addToName2(name2);
	}

	public enum NatureRuntimePropertyEnum implements TumlRuntimeProperty {
		name1(true,false,"org__tinker__qualifiertest__Nature__name1",false,false,true,false,1,1),
		god(false,false,"A_<god>_<nature>",false,false,true,false,1,1),
		name2(true,false,"org__tinker__qualifiertest__Nature__name2",false,false,true,false,1,1);
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
		private NatureRuntimePropertyEnum(boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower) {
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
			if ( name1.getLabel().equals(label) ) {
				return name1;
			}
			if ( god.getLabel().equals(label) ) {
				return god;
			}
			if ( name2.getLabel().equals(label) ) {
				return name2;
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