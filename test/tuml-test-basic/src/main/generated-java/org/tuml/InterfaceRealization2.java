package org.tuml;

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

public class InterfaceRealization2 extends BaseTinker implements CompositionNode, Interface2 {
	private TinkerSet<String> name;
	private TinkerSet<Interface1> interface1;

	/** Constructor for InterfaceRealization2
	 * 
	 * @param compositeOwner 
	 */
	public InterfaceRealization2(Interface1 compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		createComponents();
		initialiseProperties();
		init(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/** Constructor for InterfaceRealization2
	 * 
	 * @param vertex 
	 */
	public InterfaceRealization2(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/** Default constructor for InterfaceRealization2
	 */
	public InterfaceRealization2() {
	}
	
	/** Constructor for InterfaceRealization2
	 * 
	 * @param persistent 
	 */
	public InterfaceRealization2(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
		initialiseProperties();
		createComponents();
	}

	public void addToInterface1(Interface1 interface1) {
		if ( interface1 != null ) {
			this.interface1.add(interface1);
		}
	}
	
	public void addToName(String name) {
		if ( name != null ) {
			this.name.add(name);
		}
	}
	
	public void clearInterface1() {
		this.interface1.clear();
	}
	
	public void clearName() {
		this.name.clear();
	}
	
	public void createComponents() {
	}
	
	@Override
	public void delete() {
	}
	
	@Override
	public Long getId() {
		return TinkerIdUtilFactory.getIdUtil().getId(this.vertex);
	}
	
	public Interface1 getInterface1() {
		TinkerSet<Interface1> tmp = this.interface1;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
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
		return getInterface1();
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
		this.addToInterface1((Interface1)compositeOwner);
		this.hasInitBeenCalled = true;
		initVariables();
	}
	
	public void initVariables() {
	}
	
	@Override
	public void initialiseProperties() {
		this.name =  new TinkerSetImpl<String>(this, InterfaceRealization2RuntimePropertyEnum.name);
		this.interface1 =  new TinkerSetImpl<Interface1>(this, InterfaceRealization2RuntimePropertyEnum.interface1);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (InterfaceRealization2RuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case interface1:
				this.interface1 =  new TinkerSetImpl<Interface1>(this, InterfaceRealization2RuntimePropertyEnum.interface1);
			break;
		
			case name:
				this.name =  new TinkerSetImpl<String>(this, InterfaceRealization2RuntimePropertyEnum.name);
			break;
		
		}
	}
	
	@Override
	public boolean isTinkerRoot() {
		return false;
	}
	
	public void removeFromInterface1(Interface1 interface1) {
		if ( interface1 != null ) {
			this.interface1.remove(interface1);
		}
	}
	
	public void removeFromInterface1(Set<Interface1> interface1) {
		if ( !interface1.isEmpty() ) {
			this.interface1.removeAll(interface1);
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
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}
	
	public void setInterface1(Interface1 interface1) {
		clearInterface1();
		addToInterface1(interface1);
	}
	
	public void setName(String name) {
		clearName();
		addToName(name);
	}

	public enum InterfaceRealization2RuntimePropertyEnum implements TumlRuntimeProperty {
		name(true,false,"org__tuml__Interface2__name",false,false,true,false,1,1),
		interface1(false,false,"A_<interface1>_<interface2>",false,false,true,false,1,1);
		private boolean controllingSide;
		private boolean composite;
		private String label;
		private boolean oneToOne;
		private boolean oneToMany;
		private boolean manyToOne;
		private boolean manyToMany;
		private int upper;
		private int lower;
		/** Constructor for InterfaceRealization2RuntimePropertyEnum
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
		private InterfaceRealization2RuntimePropertyEnum(boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower) {
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
	
		static public InterfaceRealization2RuntimePropertyEnum fromLabel(String label) {
			if ( name.getLabel().equals(label) ) {
				return name;
			}
			if ( interface1.getLabel().equals(label) ) {
				return interface1;
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