package org.tinker.inheritencetest;

import com.tinkerpop.blueprints.Vertex;

import org.tinker.concretetest.God;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.CompositionNode;
import org.tuml.runtime.domain.TinkerNode;

public class Quadped extends Mamal implements CompositionNode {


	/** Constructor for Quadped
	 * 
	 * @param compositeOwner 
	 */
	public Quadped(God compositeOwner) {
		super(true);
		init(compositeOwner);
	}
	
	/** Constructor for Quadped
	 * 
	 * @param vertex 
	 */
	public Quadped(Vertex vertex) {
		super(vertex);
		initialiseProperties();
	}
	
	/** Default constructor for Quadped
	 */
	public Quadped() {
		super.initVariables();
	}
	
	/** Constructor for Quadped
	 * 
	 * @param persistent 
	 */
	public Quadped(Boolean persistent) {
		super( persistent );
	}

	public void createComponents() {
		super.createComponents();
	}
	
	@Override
	public void delete() {
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
		this.addToGod((God)compositeOwner);
		this.hasInitBeenCalled = true;
		initVariables();
	}
	
	public void initVariables() {
		super.initVariables();
	}
	
	@Override
	public void initialiseProperties() {
		super.initialiseProperties();
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		super.initialiseProperties();
		switch ( (QuadpedRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
		}
	}
	
	@Override
	public boolean isTinkerRoot() {
		return false;
	}

	public enum QuadpedRuntimePropertyEnum implements TumlRuntimeProperty {
	;
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
		/** Constructor for QuadpedRuntimePropertyEnum
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
		private QuadpedRuntimePropertyEnum(boolean onePrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower) {
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
	
		static public QuadpedRuntimePropertyEnum fromLabel(String label) {
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