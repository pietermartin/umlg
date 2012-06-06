package org.tinker.hierarchytest;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;

import java.util.Set;
import java.util.UUID;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TinkerSetImpl;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.TinkerNode;

public class AbstractFolder extends BaseTinker implements TinkerNode, Hierarchy {
	private TinkerSet<String> name;
	private TinkerSet<Folder> childFolder;

	/** Constructor for AbstractFolder
	 * 
	 * @param vertex 
	 */
	public AbstractFolder(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/** Default constructor for AbstractFolder
	 */
	public AbstractFolder() {
	}
	
	/** Constructor for AbstractFolder
	 * 
	 * @param persistent 
	 */
	public AbstractFolder(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		defaultCreate();
		initialiseProperties();
		createComponents();
		Edge edge = GraphDb.getDb().addEdge(null, GraphDb.getDb().getRoot(), this.vertex, "root");
		edge.setProperty("inClass", this.getClass().getName());
	}

	public void addToChildFolder(Folder childFolder) {
		if ( childFolder != null ) {
			this.childFolder.add(childFolder);
		}
	}
	
	public void addToChildFolder(Set<Folder> childFolder) {
		if ( !childFolder.isEmpty() ) {
			this.childFolder.addAll(childFolder);
		}
	}
	
	public void addToName(String name) {
		if ( name != null ) {
			this.name.add(name);
		}
	}
	
	public void clearChildFolder() {
		this.childFolder.clear();
	}
	
	public void clearName() {
		this.name.clear();
	}
	
	public void createComponents() {
	}
	
	@Override
	public void delete() {
	}
	
	public TinkerSet<Folder> getChildFolder() {
		return this.childFolder;
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
		this.name =  new TinkerSetImpl<String>(this, AbstractFolderRuntimePropertyEnum.name);
		this.childFolder =  new TinkerSetImpl<Folder>(this, AbstractFolderRuntimePropertyEnum.childFolder);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (AbstractFolderRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case childFolder:
				this.childFolder =  new TinkerSetImpl<Folder>(this, AbstractFolderRuntimePropertyEnum.childFolder);
			break;
		
			case name:
				this.name =  new TinkerSetImpl<String>(this, AbstractFolderRuntimePropertyEnum.name);
			break;
		
		}
	}
	
	@Override
	public boolean isTinkerRoot() {
		return true;
	}
	
	public void removeFromChildFolder(Folder childFolder) {
		if ( childFolder != null ) {
			this.childFolder.remove(childFolder);
		}
	}
	
	public void removeFromChildFolder(Set<Folder> childFolder) {
		if ( !childFolder.isEmpty() ) {
			this.childFolder.removeAll(childFolder);
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
	
	public void setChildFolder(Set<Folder> childFolder) {
		clearChildFolder();
		addToChildFolder(childFolder);
	}
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}
	
	public void setName(String name) {
		clearName();
		addToName(name);
	}

	public enum AbstractFolderRuntimePropertyEnum implements TumlRuntimeProperty {
		name(true,false,"org__tinker__hierarchytest__AbstractFolder__name",false,false,true,false,1,1),
		childFolder(true,true,"A_<abstractFolder>_<folder>",false,true,false,false,-1,0);
		private boolean controllingSide;
		private boolean composite;
		private String label;
		private boolean oneToOne;
		private boolean oneToMany;
		private boolean manyToOne;
		private boolean manyToMany;
		private int upper;
		private int lower;
		/** Constructor for AbstractFolderRuntimePropertyEnum
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
		private AbstractFolderRuntimePropertyEnum(boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower) {
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
	
		static public AbstractFolderRuntimePropertyEnum fromLabel(String label) {
			if ( name.getLabel().equals(label) ) {
				return name;
			}
			if ( childFolder.getLabel().equals(label) ) {
				return childFolder;
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