package org.tinker.hierarchytest;

import com.tinkerpop.blueprints.pgm.Vertex;

import java.util.Set;

import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TinkerSetImpl;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.CompositionNode;
import org.tuml.runtime.domain.TinkerNode;

public class Folder extends AbstractFolder implements CompositionNode {
	private TinkerSet<AbstractFolder> parentFolder;

	/** Constructor for Folder
	 * 
	 * @param compositeOwner 
	 */
	public Folder(AbstractFolder compositeOwner) {
		super(true);
		initialiseProperties();
		init(compositeOwner);
	}
	
	/** Constructor for Folder
	 * 
	 * @param vertex 
	 */
	public Folder(Vertex vertex) {
		super(vertex);
		initialiseProperties();
	}
	
	/** Default constructor for Folder
	 */
	public Folder() {
		super.initVariables();
	}
	
	/** Constructor for Folder
	 * 
	 * @param persistent 
	 */
	public Folder(Boolean persistent) {
		super( persistent );
		initialiseProperties();
	}

	public void addToParentFolder(AbstractFolder parentFolder) {
		if ( parentFolder != null ) {
			this.parentFolder.add(parentFolder);
		}
	}
	
	public void clearParentFolder() {
		this.parentFolder.clear();
	}
	
	public void createComponents() {
		super.createComponents();
	}
	
	@Override
	public void delete() {
	}
	
	@Override
	public TinkerNode getOwningObject() {
		return getParentFolder();
	}
	
	public AbstractFolder getParentFolder() {
		TinkerSet<AbstractFolder> tmp = this.parentFolder;
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
		this.parentFolder.add((AbstractFolder)compositeOwner);
		this.hasInitBeenCalled = true;
		initVariables();
	}
	
	public void initVariables() {
		super.initVariables();
	}
	
	@Override
	public void initialiseProperties() {
		super.initialiseProperties();
		this.parentFolder =  new TinkerSetImpl<AbstractFolder>(this, FolderRuntimePropertyEnum.parentFolder);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		super.initialiseProperties();
		switch ( (FolderRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case parentFolder:
				this.parentFolder =  new TinkerSetImpl<AbstractFolder>(this, FolderRuntimePropertyEnum.parentFolder);
			break;
		
		}
	}
	
	@Override
	public boolean isTinkerRoot() {
		return false;
	}
	
	public void removeFromParentFolder(AbstractFolder parentFolder) {
		if ( parentFolder != null ) {
			this.parentFolder.remove(parentFolder);
		}
	}
	
	public void removeFromParentFolder(Set<AbstractFolder> parentFolder) {
		if ( !parentFolder.isEmpty() ) {
			this.parentFolder.removeAll(parentFolder);
		}
	}
	
	public void setParentFolder(AbstractFolder parentFolder) {
		clearParentFolder();
		addToParentFolder(parentFolder);
	}

	public enum FolderRuntimePropertyEnum implements TumlRuntimeProperty {
		parentFolder(false,false,"A_<abstractFolder>_<folder>",false,false,true,false,1,1);
		private boolean controllingSide;
		private boolean composite;
		private String label;
		private boolean oneToOne;
		private boolean oneToMany;
		private boolean manyToOne;
		private boolean manyToMany;
		private int upper;
		private int lower;
		/** Constructor for FolderRuntimePropertyEnum
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
		private FolderRuntimePropertyEnum(boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower) {
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
	
		static public FolderRuntimePropertyEnum fromLabel(String label) {
			if ( parentFolder.getLabel().equals(label) ) {
				return parentFolder;
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