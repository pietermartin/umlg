package org.tuml.hierarchytest;

import com.tinkerpop.blueprints.Vertex;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.tuml.runtime.collection.Qualifier;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.collection.persistent.TinkerSetImpl;
import org.tuml.runtime.domain.CompositionNode;
import org.tuml.runtime.domain.TumlNode;

public class Folder extends AbstractFolder implements CompositionNode {
	static final public long serialVersionUID = 1L;
	private TinkerSet<AbstractFolder> parentFolder;

	/**
	 * constructor for Folder
	 * 
	 * @param compositeOwner 
	 */
	public Folder(AbstractFolder compositeOwner) {
		super(true);
		addToParentFolder(compositeOwner);
	}
	
	/**
	 * constructor for Folder
	 * 
	 * @param vertex 
	 */
	public Folder(Vertex vertex) {
		super(vertex);
		initialiseProperties();
	}
	
	/**
	 * default constructor for Folder
	 */
	public Folder() {
		super.initVariables();
	}
	
	/**
	 * constructor for Folder
	 * 
	 * @param persistent 
	 */
	public Folder(Boolean persistent) {
		super( persistent );
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
		this.parentFolder.clear();
		super.delete();
	}
	
	@Override
	public void fromJson(Map<String,Object> propertyMap) {
		super.fromJson(propertyMap);
	}
	
	@Override
	public void fromJson(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			@SuppressWarnings(	"unchecked")
			 Map<String,Object> propertyMap = mapper.readValue(json, Map.class);
			fromJson(propertyMap);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public TumlNode getOwningObject() {
		return getParentFolder();
	}
	
	/**
	 * Implements the ocl statement for operation body condition 'getParent'
	 * <pre>
	 * package tumltest::org::tuml::hierarchytest
	 * context Folder::getParent() : Hierarchy
	 * body: self.parentFolder
	 * endpackage
	 * </pre>
	 */
	public Hierarchy getParent() {
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
	
	/**
	 * getQualifiers is called from the collection in order to update the index used to implement the qualifier
	 * 
	 * @param tumlRuntimeProperty 
	 * @param node 
	 */
	@Override
	public List<Qualifier> getQualifiers(TumlRuntimeProperty tumlRuntimeProperty, TumlNode node) {
		List<Qualifier> result = super.getQualifiers(tumlRuntimeProperty, node);
		FolderRuntimePropertyEnum runtimeProperty = FolderRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
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
	 * getSize is called from the collection in order to update the index used to implement a sequence's index
	 * 
	 * @param tumlRuntimeProperty 
	 */
	@Override
	public int getSize(TumlRuntimeProperty tumlRuntimeProperty) {
		int result = super.getSize(tumlRuntimeProperty);
		FolderRuntimePropertyEnum runtimeProperty = FolderRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result == 0 ) {
			switch ( runtimeProperty ) {
				case parentFolder:
					result = parentFolder.size();
				break;
			
				default:
					result = 0;
				break;
			}
		
		}
		return result;
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
		super.initialiseProperty(tumlRuntimeProperty);
		switch ( (FolderRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case parentFolder:
				this.parentFolder =  new TinkerSetImpl<AbstractFolder>(this, FolderRuntimePropertyEnum.parentFolder);
			break;
		
		}
	}
	
	/**
	 * Implements the ocl statement for operation body condition 'isRoot'
	 * <pre>
	 * package tumltest::org::tuml::hierarchytest
	 * context Folder::isRoot() : Boolean
	 * body: false
	 * endpackage
	 * </pre>
	 */
	public Boolean isRoot() {
		return false;
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
	
	public void removeFromParentFolder(TinkerSet<AbstractFolder> parentFolder) {
		if ( !parentFolder.isEmpty() ) {
			this.parentFolder.removeAll(parentFolder);
		}
	}
	
	public void setParentFolder(AbstractFolder parentFolder) {
		clearParentFolder();
		addToParentFolder(parentFolder);
	}
	
	/**
	 * Implements the ocl statement for operation body condition 'testQueryOperation'
	 * <pre>
	 * package tumltest::org::tuml::hierarchytest
	 * context Folder::testQueryOperation(test: String) : String
	 * body: self.name
	 * endpackage
	 * </pre>
	 * 
	 * @param test 
	 */
	public String testQueryOperation(String test) {
		return getName();
	}
	
	@Override
	public String toJson() {
		String result = super.toJson();
		result = result.substring(1, result.length() - 1);
		StringBuilder sb = new StringBuilder(result);
		sb.append("{");
		sb.append("\"id\": " + getId() + ", ");
		sb.append("}");
		return sb.toString();
	}

	static public enum FolderRuntimePropertyEnum implements TumlRuntimeProperty {
		parentFolder(false,false,false,false,"A_<abstractFolder>_<folder>",false,false,true,false,1,1,false,false,false,false,true,"{\"parentFolder\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": false, \"composite\": false, \"oneToOne\": false, \"oneToMany\": false, \"manyToOne\": true, \"manyToMany\": false, \"upper\": 1, \"lower\": 1, \"label\": \"A_<abstractFolder>_<folder>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}");
		private boolean onePrimitive;
		private boolean manyPrimitive;
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
		private String json;
		/**
		 * constructor for FolderRuntimePropertyEnum
		 * 
		 * @param onePrimitive 
		 * @param manyPrimitive 
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
		 * @param json 
		 */
		private FolderRuntimePropertyEnum(boolean onePrimitive, boolean manyPrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower, boolean qualified, boolean inverseQualified, boolean ordered, boolean inverseOrdered, boolean unique, String json) {
			this.onePrimitive = onePrimitive;
			this.manyPrimitive = manyPrimitive;
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
			this.json = json;
		}
	
		static public String asJson() {
			StringBuilder sb = new StringBuilder();;
			sb.append("{\"Folder\": [");
			sb.append(FolderRuntimePropertyEnum.parentFolder.toJson());
			sb.append("]}");
			return sb.toString();
		}
		
		static public FolderRuntimePropertyEnum fromLabel(String label) {
			if ( parentFolder.getLabel().equals(label) ) {
				return parentFolder;
			}
			return null;
		}
		
		public String getJson() {
			return this.json;
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
		
		public boolean isManyPrimitive() {
			return this.manyPrimitive;
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
			if ( isQualified() ) {
				return elementCount >= getLower();
			} else {
				return (getUpper() == -1 || elementCount <= getUpper()) && elementCount >= getLower();
			}
		}
		
		@Override
		public String toJson() {
			return getJson();
		}
	
	}
}