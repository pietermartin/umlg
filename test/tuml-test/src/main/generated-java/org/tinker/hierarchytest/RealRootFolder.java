package org.tinker.hierarchytest;

import com.tinkerpop.blueprints.pgm.Vertex;

import java.util.Set;

import org.tinker.concretetest.God;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TinkerSetImpl;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.CompositionNode;
import org.tuml.runtime.domain.TinkerNode;

public class RealRootFolder extends AbstractRootFolder implements CompositionNode {
	private TinkerSet<God> god;

	/** Constructor for RealRootFolder
	 * 
	 * @param compositeOwner 
	 */
	public RealRootFolder(God compositeOwner) {
		super(true);
		initialiseProperties();
		init(compositeOwner);
	}
	
	/** Constructor for RealRootFolder
	 * 
	 * @param vertex 
	 */
	public RealRootFolder(Vertex vertex) {
		super(vertex);
		initialiseProperties();
	}
	
	/** Default constructor for RealRootFolder
	 */
	public RealRootFolder() {
		super.initVariables();
	}
	
	/** Constructor for RealRootFolder
	 * 
	 * @param persistent 
	 */
	public RealRootFolder(Boolean persistent) {
		super( persistent );
		initialiseProperties();
	}

	public void addToGod(God god) {
		if ( god != null ) {
			this.god.add(god);
		}
	}
	
	public void clearGod() {
		this.god.clear();
	}
	
	public void createComponents() {
		super.createComponents();
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
	public TinkerNode getOwningObject() {
		return getGod();
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
		super.initVariables();
	}
	
	@Override
	public void initialiseProperties() {
		super.initialiseProperties();
		this.god =  new TinkerSetImpl<God>(this, RealRootFolderRuntimePropertyEnum.GOD);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		super.initialiseProperties();
		switch ( (RealRootFolderRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case GOD:
				this.god =  new TinkerSetImpl<God>(this, RealRootFolderRuntimePropertyEnum.GOD);
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
	
	public void setGod(God god) {
		clearGod();
		addToGod(god);
	}

	public enum RealRootFolderRuntimePropertyEnum implements TumlRuntimeProperty {
		GOD(false,false,"A_<god>_<realRootFolder>",false,false,true,false,1,1);
		private boolean controllingSide;
		private boolean composite;
		private String label;
		private boolean oneToOne;
		private boolean oneToMany;
		private boolean manyToOne;
		private boolean manyToMany;
		private int upper;
		private int lower;
		/** Constructor for RealRootFolderRuntimePropertyEnum
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
		private RealRootFolderRuntimePropertyEnum(boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower) {
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
	
		static public RealRootFolderRuntimePropertyEnum fromLabel(String label) {
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