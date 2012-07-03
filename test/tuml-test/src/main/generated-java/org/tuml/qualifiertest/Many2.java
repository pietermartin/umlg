package org.tuml.qualifiertest;

import com.tinkerpop.blueprints.CloseableIterable;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Index;
import com.tinkerpop.blueprints.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.tuml.concretetest.God;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.collection.Multiplicity;
import org.tuml.runtime.collection.Qualifier;
import org.tuml.runtime.collection.TinkerQualifiedSequence;
import org.tuml.runtime.collection.TinkerQualifiedSequenceImpl;
import org.tuml.runtime.collection.TinkerQualifiedSet;
import org.tuml.runtime.collection.TinkerQualifiedSetImpl;
import org.tuml.runtime.collection.TinkerSequence;
import org.tuml.runtime.collection.TinkerSequenceClosableIterableImpl;
import org.tuml.runtime.collection.TinkerSequenceImpl;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TinkerSetClosableIterableImpl;
import org.tuml.runtime.collection.TinkerSetImpl;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.CompositionNode;
import org.tuml.runtime.domain.TinkerNode;

public class Many2 extends BaseTinker implements CompositionNode {
	static final public long serialVersionUID = 1L;
	private TinkerSet<String> name;
	private TinkerSet<God> god;
	private TinkerQualifiedSet<Many1> many1;
	private TinkerQualifiedSequence<Many1> many1List;
	private TinkerSequence<Many1> many1UnqualifiedList;

	/** Constructor for Many2
	 * 
	 * @param compositeOwner 
	 */
	public Many2(God compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		initialiseProperties();
		createComponents();
		init(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/** Constructor for Many2
	 * 
	 * @param vertex 
	 */
	public Many2(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/** Default constructor for Many2
	 */
	public Many2() {
	}
	
	/** Constructor for Many2
	 * 
	 * @param persistent 
	 */
	public Many2(Boolean persistent) {
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
	
	public void addToMany1(Many1 many1) {
		if ( many1 != null ) {
			this.many1.add(many1);
		}
	}
	
	public void addToMany1(Set<Many1> many1) {
		for ( Many1 m : many1 ) {
			this.addToMany1(m);
		}
	}
	
	public void addToMany1List(List<Many1> many1List) {
		for ( Many1 m : many1List ) {
			this.addToMany1List(m);
		}
	}
	
	public void addToMany1List(Many1 many1List) {
		if ( many1List != null ) {
			this.many1List.add(many1List);
		}
	}
	
	public void addToMany1UnqualifiedList(List<Many1> many1UnqualifiedList) {
		if ( !many1UnqualifiedList.isEmpty() ) {
			this.many1UnqualifiedList.addAll(many1UnqualifiedList);
		}
	}
	
	public void addToMany1UnqualifiedList(Many1 many1UnqualifiedList) {
		if ( many1UnqualifiedList != null ) {
			this.many1UnqualifiedList.add(many1UnqualifiedList);
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
	
	public void clearMany1() {
		this.many1.clear();
	}
	
	public void clearMany1List() {
		this.many1List.clear();
	}
	
	public void clearMany1UnqualifiedList() {
		this.many1UnqualifiedList.clear();
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
	
	public String getListQualifier2() {
		return getName();
	}
	
	public TinkerQualifiedSet<Many1> getMany1() {
		return this.many1;
	}
	
	public Set<Many1> getMany1ForQualifier1(String qualifier1) {
		Index<Edge> index = GraphDb.getDb().getIndex(getUid() + ":::" + Many2RuntimePropertyEnum.many1.getLabel(), Edge.class);
		if ( index==null ) {
			return null;
		} else {
			CloseableIterable<Edge> closeableIterable = index.get("qualifier1", qualifier1==null?"___NULL___":qualifier1);
			if ( closeableIterable.iterator().hasNext() ) {
				return new TinkerSetClosableIterableImpl<Many1>(closeableIterable, Many2RuntimePropertyEnum.many1);
			} else {
				return Collections.emptySet();
			}
		}
	}
	
	public TinkerQualifiedSequence<Many1> getMany1List() {
		return this.many1List;
	}
	
	public List<Many1> getMany1ListForListQualifier1(String listQualifier1) {
		Index<Edge> index = GraphDb.getDb().getIndex(getUid() + ":::" + Many2RuntimePropertyEnum.many1List.getLabel(), Edge.class);
		if ( index==null ) {
			return null;
		} else {
			CloseableIterable<Edge> closeableIterable = index.get("listQualifier1", listQualifier1==null?"___NULL___":listQualifier1);
			if ( closeableIterable.iterator().hasNext() ) {
				return new TinkerSequenceClosableIterableImpl<Many1>(closeableIterable, Many2RuntimePropertyEnum.many1List);
			} else {
				return Collections.emptyList();
			}
		}
	}
	
	public TinkerSequence<Many1> getMany1UnqualifiedList() {
		return this.many1UnqualifiedList;
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
	
	public String getQualifier1() {
		return getName();
	}
	
	public List<Qualifier> getQualifierForMany1(Many1 context) {
		List<Qualifier> result = new ArrayList<Qualifier>();
		result.add(new Qualifier("qualifier1", context.getQualifier1(), Multiplicity.ZERO_TO_MANY));
		return result;
	}
	
	public List<Qualifier> getQualifierForMany1List(Many1 context) {
		List<Qualifier> result = new ArrayList<Qualifier>();
		result.add(new Qualifier("listQualifier1", context.getListQualifier1(), Multiplicity.ZERO_TO_MANY));
		return result;
	}
	
	/** GetQualifiers is called from the collection in order to update the index used to implement the qualifier
	 * 
	 * @param tumlRuntimeProperty 
	 * @param node 
	 */
	@Override
	public List<Qualifier> getQualifiers(TumlRuntimeProperty tumlRuntimeProperty, TinkerNode node) {
		List<Qualifier> result = Collections.emptyList();
		Many2RuntimePropertyEnum runtimeProperty = Many2RuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result.isEmpty() ) {
			switch ( runtimeProperty ) {
				case many1List:
					result = getQualifierForMany1List((Many1)node);
				break;
			
				case many1:
					result = getQualifierForMany1((Many1)node);
				break;
			
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
		Many2RuntimePropertyEnum runtimeProperty = Many2RuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result == 0 ) {
			switch ( runtimeProperty ) {
				case many1List:
					result = many1List.size();
				break;
			
				case many1UnqualifiedList:
					result = many1UnqualifiedList.size();
				break;
			
				case god:
					result = god.size();
				break;
			
				case name:
					result = name.size();
				break;
			
				case many1:
					result = many1.size();
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
		this.many1 =  new TinkerQualifiedSetImpl<Many1>(this, Many2RuntimePropertyEnum.many1);
		this.name =  new TinkerSetImpl<String>(this, Many2RuntimePropertyEnum.name);
		this.god =  new TinkerSetImpl<God>(this, Many2RuntimePropertyEnum.god);
		this.many1UnqualifiedList =  new TinkerSequenceImpl<Many1>(this, Many2RuntimePropertyEnum.many1UnqualifiedList);
		this.many1List =  new TinkerQualifiedSequenceImpl<Many1>(this, Many2RuntimePropertyEnum.many1List);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (Many2RuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case many1List:
				this.many1List =  new TinkerQualifiedSequenceImpl<Many1>(this, Many2RuntimePropertyEnum.many1List);
			break;
		
			case many1UnqualifiedList:
				this.many1UnqualifiedList =  new TinkerSequenceImpl<Many1>(this, Many2RuntimePropertyEnum.many1UnqualifiedList);
			break;
		
			case god:
				this.god =  new TinkerSetImpl<God>(this, Many2RuntimePropertyEnum.god);
			break;
		
			case name:
				this.name =  new TinkerSetImpl<String>(this, Many2RuntimePropertyEnum.name);
			break;
		
			case many1:
				this.many1 =  new TinkerQualifiedSetImpl<Many1>(this, Many2RuntimePropertyEnum.many1);
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
	
	public void removeFromMany1(Many1 many1) {
		if ( many1 != null ) {
			this.many1.remove(many1);
		}
	}
	
	public void removeFromMany1(Set<Many1> many1) {
		if ( !many1.isEmpty() ) {
			this.many1.removeAll(many1);
		}
	}
	
	public void removeFromMany1List(List<Many1> many1List) {
		if ( !many1List.isEmpty() ) {
			this.many1List.removeAll(many1List);
		}
	}
	
	public void removeFromMany1List(Many1 many1List) {
		if ( many1List != null ) {
			this.many1List.remove(many1List);
		}
	}
	
	public void removeFromMany1UnqualifiedList(List<Many1> many1UnqualifiedList) {
		if ( !many1UnqualifiedList.isEmpty() ) {
			this.many1UnqualifiedList.removeAll(many1UnqualifiedList);
		}
	}
	
	public void removeFromMany1UnqualifiedList(Many1 many1UnqualifiedList) {
		if ( many1UnqualifiedList != null ) {
			this.many1UnqualifiedList.remove(many1UnqualifiedList);
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
	
	public void setMany1(Set<Many1> many1) {
		clearMany1();
		addToMany1(many1);
	}
	
	public void setMany1List(List<Many1> many1List) {
		clearMany1List();
		addToMany1List(many1List);
	}
	
	public void setMany1UnqualifiedList(List<Many1> many1UnqualifiedList) {
		clearMany1UnqualifiedList();
		addToMany1UnqualifiedList(many1UnqualifiedList);
	}
	
	public void setName(String name) {
		clearName();
		addToName(name);
	}

	public enum Many2RuntimePropertyEnum implements TumlRuntimeProperty {
		many1(false,true,false,"A_<many1>_<many2>",false,false,false,true,-1,0,true,true,false,false,true),
		name(true,true,false,"tuml-test__org__tuml__qualifiertest__Many2__name",false,false,true,false,1,1,false,false,false,false,true),
		god(false,false,false,"A_<god>_<many2>",false,false,true,false,1,1,false,false,false,false,true),
		many1UnqualifiedList(false,true,false,"A_<many1>_<many2>_3",false,false,false,true,-1,0,false,false,true,true,false),
		many1List(false,true,false,"A_<many1>_<many2>_2",false,false,false,true,-1,0,true,true,true,true,false);
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
		/** Constructor for Many2RuntimePropertyEnum
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
		private Many2RuntimePropertyEnum(boolean onePrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower, boolean qualified, boolean inverseQualified, boolean ordered, boolean inverseOrdered, boolean unique) {
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
	
		static public Many2RuntimePropertyEnum fromLabel(String label) {
			if ( many1.getLabel().equals(label) ) {
				return many1;
			}
			if ( name.getLabel().equals(label) ) {
				return name;
			}
			if ( god.getLabel().equals(label) ) {
				return god;
			}
			if ( many1UnqualifiedList.getLabel().equals(label) ) {
				return many1UnqualifiedList;
			}
			if ( many1List.getLabel().equals(label) ) {
				return many1List;
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