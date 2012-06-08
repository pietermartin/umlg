package org.tinker.hierarchytest;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.TinkerNode;

public class AbstractRootFolder extends AbstractFolder implements TinkerNode {


	/** Constructor for AbstractRootFolder
	 * 
	 * @param vertex 
	 */
	public AbstractRootFolder(Vertex vertex) {
		super(vertex);
		initialiseProperties();
	}
	
	/** Default constructor for AbstractRootFolder
	 */
	public AbstractRootFolder() {
		super.initVariables();
	}
	
	/** Constructor for AbstractRootFolder
	 * 
	 * @param persistent 
	 */
	public AbstractRootFolder(Boolean persistent) {
		super( persistent );
		Edge edge = GraphDb.getDb().addEdge(null, GraphDb.getDb().getRoot(), this.vertex, "root");
		edge.setProperty("inClass", this.getClass().getName());
	}

	public void createComponents() {
		super.createComponents();
	}
	
	@Override
	public void delete() {
		GraphDb.getDb().removeVertex(this.vertex);
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
		switch ( (AbstractRootFolderRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
		}
	}
	
	@Override
	public boolean isTinkerRoot() {
		return true;
	}

	public enum AbstractRootFolderRuntimePropertyEnum implements TumlRuntimeProperty {
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
		/** Constructor for AbstractRootFolderRuntimePropertyEnum
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
		private AbstractRootFolderRuntimePropertyEnum(boolean onePrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower) {
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
	
		static public AbstractRootFolderRuntimePropertyEnum fromLabel(String label) {
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