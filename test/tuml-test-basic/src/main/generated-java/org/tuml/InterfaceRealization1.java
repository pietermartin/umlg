package org.tuml;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.collection.Qualifier;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.collection.impl.TinkerSetImpl;
import org.tuml.runtime.domain.BaseTuml;
import org.tuml.runtime.domain.TumlNode;

public class InterfaceRealization1 extends BaseTuml implements TumlNode, Interface1 {
	static final public long serialVersionUID = 1L;
	private TinkerSet<String> name;
	private TinkerSet<Interface2> interface2;

	/**
	 * constructor for InterfaceRealization1
	 * 
	 * @param vertex 
	 */
	public InterfaceRealization1(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/**
	 * default constructor for InterfaceRealization1
	 */
	public InterfaceRealization1() {
	}
	
	/**
	 * constructor for InterfaceRealization1
	 * 
	 * @param persistent 
	 */
	public InterfaceRealization1(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		defaultCreate();
		initialiseProperties();
		initVariables();
		createComponents();
		Edge edge = GraphDb.getDb().addEdge(null, GraphDb.getDb().getRoot(), this.vertex, "root");
		edge.setProperty("inClass", this.getClass().getName());
	}

	public void addToInterface2(Interface2 interface2) {
		if ( interface2 != null ) {
			this.interface2.add(interface2);
		}
	}
	
	public void addToInterface2(TinkerSet<Interface2> interface2) {
		if ( !interface2.isEmpty() ) {
			this.interface2.addAll(interface2);
		}
	}
	
	public void addToName(String name) {
		if ( name != null ) {
			this.name.add(name);
		}
	}
	
	public void clearInterface2() {
		this.interface2.clear();
	}
	
	public void clearName() {
		this.name.clear();
	}
	
	public void createComponents() {
	}
	
	@Override
	public void delete() {
		for ( Interface2 child : getInterface2() ) {
			child.delete();
		}
		GraphDb.getDb().removeVertex(this.vertex);
	}
	
	@Override
	public Long getId() {
		return TinkerIdUtilFactory.getIdUtil().getId(this.vertex);
	}
	
	public TinkerSet<Interface2> getInterface2() {
		return this.interface2;
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
	
	/**
	 * getQualifiers is called from the collection in order to update the index used to implement the qualifier
	 * 
	 * @param tumlRuntimeProperty 
	 * @param node 
	 */
	@Override
	public List<Qualifier> getQualifiers(TumlRuntimeProperty tumlRuntimeProperty, TumlNode node) {
		List<Qualifier> result = Collections.emptyList();
		InterfaceRealization1RuntimePropertyEnum runtimeProperty = InterfaceRealization1RuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result.isEmpty() ) {
			switch ( runtimeProperty ) {
				default:
					result = Collections.emptyList();
				break;
			}
		
		}
		return result;
	}
	
	/**
	 * getSize is called from the collection in order to update the index used to implement a sequance's index
	 * 
	 * @param tumlRuntimeProperty 
	 */
	@Override
	public int getSize(TumlRuntimeProperty tumlRuntimeProperty) {
		int result = 0;
		InterfaceRealization1RuntimePropertyEnum runtimeProperty = InterfaceRealization1RuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result == 0 ) {
			switch ( runtimeProperty ) {
				case interface2:
					result = interface2.size();
				break;
			
				case name:
					result = name.size();
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
		this.name =  new TinkerSetImpl<String>(this, InterfaceRealization1RuntimePropertyEnum.name);
		this.interface2 =  new TinkerSetImpl<Interface2>(this, InterfaceRealization1RuntimePropertyEnum.interface2);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (InterfaceRealization1RuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case interface2:
				this.interface2 =  new TinkerSetImpl<Interface2>(this, InterfaceRealization1RuntimePropertyEnum.interface2);
			break;
		
			case name:
				this.name =  new TinkerSetImpl<String>(this, InterfaceRealization1RuntimePropertyEnum.name);
			break;
		
		}
	}
	
	@Override
	public boolean isTinkerRoot() {
		return true;
	}
	
	public void removeFromInterface2(Interface2 interface2) {
		if ( interface2 != null ) {
			this.interface2.remove(interface2);
		}
	}
	
	public void removeFromInterface2(TinkerSet<Interface2> interface2) {
		if ( !interface2.isEmpty() ) {
			this.interface2.removeAll(interface2);
		}
	}
	
	public void removeFromName(String name) {
		if ( name != null ) {
			this.name.remove(name);
		}
	}
	
	public void removeFromName(TinkerSet<String> name) {
		if ( !name.isEmpty() ) {
			this.name.removeAll(name);
		}
	}
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}
	
	public void setInterface2(TinkerSet<Interface2> interface2) {
		clearInterface2();
		addToInterface2(interface2);
	}
	
	public void setName(String name) {
		clearName();
		addToName(name);
	}

	public enum InterfaceRealization1RuntimePropertyEnum implements TumlRuntimeProperty {
		name(true,true,false,"basicmodel__org__tuml__Interface1__name",false,false,true,false,1,1,false,false,false,false,true),
		interface2(false,true,true,"A_<interface1>_<interface2>",false,true,false,false,-1,0,false,false,false,false,true);
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
		/**
		 * constructor for InterfaceRealization1RuntimePropertyEnum
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
		private InterfaceRealization1RuntimePropertyEnum(boolean onePrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower, boolean qualified, boolean inverseQualified, boolean ordered, boolean inverseOrdered, boolean unique) {
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
	
		static public InterfaceRealization1RuntimePropertyEnum fromLabel(String label) {
			if ( name.getLabel().equals(label) ) {
				return name;
			}
			if ( interface2.getLabel().equals(label) ) {
				return interface2;
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