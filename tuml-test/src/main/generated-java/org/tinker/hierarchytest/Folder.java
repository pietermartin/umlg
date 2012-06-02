package org.tinker.hierarchytest;

import com.tinkerpop.blueprints.pgm.Vertex;

import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.domain.CompositionNode;

public class Folder extends AbstractFolder implements CompositionNode {
	private TinkerSet<AbstractFolder> parentFolder;

	/** Constructor for Folder
	 * 
	 * @param compositeOwner 
	 */
	public Folder(AbstractFolder compositeOwner) {
		super(true);
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
			parentFolder.z_internalRemoveFromChildFolder(parentFolder.getChildFolder());
			parentFolder.z_internalAddToChildFolder(this);
			z_internalAddToParentFolder(parentFolder);
		}
	}
	
	public void createComponents() {
		super.createComponents();
	}
	
	@Override
	public void delete() {
	}
	
	@Override
	public CompositionNode getOwningObject() {
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
	
	@Override
	public void init(CompositionNode compositeOwner) {
		this.z_internalAddToParentFolder((AbstractFolder)compositeOwner);
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
	public boolean isTinkerRoot() {
		return false;
	}
	
	public void setParentFolder(TinkerSet<AbstractFolder> parentFolder) {
	}
	
	public void z_internalAddToParentFolder(AbstractFolder parentFolder) {
		this.parentFolder.add(parentFolder);
	}
	
	public void z_internalRemoveFromParentFolder(AbstractFolder parentFolder) {
		this.parentFolder.remove(parentFolder);
	}

}