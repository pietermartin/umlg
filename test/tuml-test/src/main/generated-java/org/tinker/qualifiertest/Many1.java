package org.tinker.qualifiertest;

import com.tinkerpop.blueprints.Vertex;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.tinker.concretetest.God;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.collection.TinkerQualifiedSequence;
import org.tuml.runtime.collection.TinkerQualifiedSequenceImpl;
import org.tuml.runtime.collection.TinkerQualifiedSet;
import org.tuml.runtime.collection.TinkerQualifiedSetImpl;
import org.tuml.runtime.collection.TinkerSequence;
import org.tuml.runtime.collection.TinkerSequenceImpl;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TinkerSetImpl;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.CompositionNode;
import org.tuml.runtime.domain.TinkerNode;

public class Many1 extends BaseTinker implements CompositionNode {
	private TinkerSet<String> name;
	private TinkerSet<God> god;
	private TinkerQualifiedSet<Many2> many2;
	private TinkerQualifiedSequence<Many2> many2List;
	private TinkerSequence<Many2> many2UnqualifiedList;

	/** Constructor for Many1
	 * 
	 * @param compositeOwner 
	 */
	public Many1(God compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		initialiseProperties();
		createComponents();
		init(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/** Constructor for Many1
	 * 
	 * @param vertex 
	 */
	public Many1(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/** Default constructor for Many1
	 */
	public Many1() {
	}
	
	/** Constructor for Many1
	 * 
	 * @param persistent 
	 */
	public Many1(Boolean persistent) {
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
	
	public void addToMany2(Many2 many2) {
		if ( many2 != null ) {
			this.many2.add(many2);
		}
	}
	
	public void addToMany2(Set<Many2> many2) {
		if ( !many2.isEmpty() ) {
			this.many2.addAll(many2);
		}
	}
	
	public void addToMany2List(List<Many2> many2List) {
		if ( !many2List.isEmpty() ) {
			this.many2List.addAll(many2List);
		}
	}
	
	public void addToMany2List(Many2 many2List) {
		if ( many2List != null ) {
			this.many2List.add(many2List);
		}
	}
	
	public void addToMany2UnqualifiedList(List<Many2> many2UnqualifiedList) {
		if ( !many2UnqualifiedList.isEmpty() ) {
			this.many2UnqualifiedList.addAll(many2UnqualifiedList);
		}
	}
	
	public void addToMany2UnqualifiedList(Many2 many2UnqualifiedList) {
		if ( many2UnqualifiedList != null ) {
			this.many2UnqualifiedList.add(many2UnqualifiedList);
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
	
	public void clearMany2() {
		this.many2.clear();
	}
	
	public void clearMany2List() {
		this.many2List.clear();
	}
	
	public void clearMany2UnqualifiedList() {
		this.many2UnqualifiedList.clear();
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
	
	@Override
	public Long getId() {
		return TinkerIdUtilFactory.getIdUtil().getId(this.vertex);
	}
	
	public TinkerQualifiedSet<Many2> getMany2() {
		return this.many2;
	}
	
	public TinkerQualifiedSequence<Many2> getMany2List() {
		return this.many2List;
	}
	
	public TinkerSequence<Many2> getMany2UnqualifiedList() {
		return this.many2UnqualifiedList;
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
		this.many2 =  new TinkerQualifiedSetImpl<Many2>(this, getUid(), Many1RuntimePropertyEnum.many2);
		this.many2List =  new TinkerQualifiedSequenceImpl<Many2>(this, getUid(), Many1RuntimePropertyEnum.many2List);
		this.many2UnqualifiedList =  new TinkerSequenceImpl<Many2>(this, getUid(), Many1RuntimePropertyEnum.many2UnqualifiedList);
		this.name =  new TinkerSetImpl<String>(this, Many1RuntimePropertyEnum.name);
		this.god =  new TinkerSetImpl<God>(this, Many1RuntimePropertyEnum.god);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (Many1RuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case god:
				this.god =  new TinkerSetImpl<God>(this, Many1RuntimePropertyEnum.god);
			break;
		
			case name:
				this.name =  new TinkerSetImpl<String>(this, Many1RuntimePropertyEnum.name);
			break;
		
			case many2UnqualifiedList:
				this.many2UnqualifiedList =  new TinkerSequenceImpl<Many2>(this, getUid(), Many1RuntimePropertyEnum.many2UnqualifiedList);
			break;
		
			case many2List:
				this.many2List =  new TinkerQualifiedSequenceImpl<Many2>(this, getUid(), Many1RuntimePropertyEnum.many2List);
			break;
		
			case many2:
				this.many2 =  new TinkerQualifiedSetImpl<Many2>(this, getUid(), Many1RuntimePropertyEnum.many2);
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
	
	public void removeFromMany2(Many2 many2) {
		if ( many2 != null ) {
			this.many2.remove(many2);
		}
	}
	
	public void removeFromMany2(Set<Many2> many2) {
		if ( !many2.isEmpty() ) {
			this.many2.removeAll(many2);
		}
	}
	
	public void removeFromMany2List(List<Many2> many2List) {
		if ( !many2List.isEmpty() ) {
			this.many2List.removeAll(many2List);
		}
	}
	
	public void removeFromMany2List(Many2 many2List) {
		if ( many2List != null ) {
			this.many2List.remove(many2List);
		}
	}
	
	public void removeFromMany2UnqualifiedList(List<Many2> many2UnqualifiedList) {
		if ( !many2UnqualifiedList.isEmpty() ) {
			this.many2UnqualifiedList.removeAll(many2UnqualifiedList);
		}
	}
	
	public void removeFromMany2UnqualifiedList(Many2 many2UnqualifiedList) {
		if ( many2UnqualifiedList != null ) {
			this.many2UnqualifiedList.remove(many2UnqualifiedList);
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
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}
	
	public void setMany2(Set<Many2> many2) {
		clearMany2();
		addToMany2(many2);
	}
	
	public void setMany2List(List<Many2> many2List) {
		clearMany2List();
		addToMany2List(many2List);
	}
	
	public void setMany2UnqualifiedList(List<Many2> many2UnqualifiedList) {
		clearMany2UnqualifiedList();
		addToMany2UnqualifiedList(many2UnqualifiedList);
	}
	
	public void setName(String name) {
		clearName();
		addToName(name);
	}

	public enum Many1RuntimePropertyEnum implements TumlRuntimeProperty {
		many2(false,false,false,"A_<many1>_<many2>",false,false,false,true,-1,0),
		many2List(false,false,false,"A_<many1>_<many2>_2",false,false,false,true,-1,0),
		many2UnqualifiedList(false,false,false,"A_<many1>_<many2>_3",false,false,false,true,-1,0),
		name(true,true,false,"org__tinker__qualifiertest__Many1__name",false,false,true,false,1,1),
		god(false,false,false,"A_<god>_<many1>",false,false,true,false,1,1);
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
		/** Constructor for Many1RuntimePropertyEnum
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
		private Many1RuntimePropertyEnum(boolean onePrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower) {
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
	
		static public Many1RuntimePropertyEnum fromLabel(String label) {
			if ( many2.getLabel().equals(label) ) {
				return many2;
			}
			if ( many2List.getLabel().equals(label) ) {
				return many2List;
			}
			if ( many2UnqualifiedList.getLabel().equals(label) ) {
				return many2UnqualifiedList;
			}
			if ( name.getLabel().equals(label) ) {
				return name;
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