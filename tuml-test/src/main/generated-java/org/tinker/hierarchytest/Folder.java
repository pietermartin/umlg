package org.tinker.hierarchytest;

import com.tinkerpop.blueprints.pgm.Vertex;

import org.tuml.runtime.domain.CompositionNode;

public class Folder extends AbstractFolder implements CompositionNode {


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
	}

	@Override
	public void clearCache() {
		super.clearCache();
	}
	
	public void createComponents() {
		super.createComponents();
	}
	
	public void init(AbstractFolder compositeOwner) {
		this.z_internalAddToParentFolder(owner);
		this.hasInitBeenCalled = true;
		initVariables();
	}
	
	public void initVariables() {
		super.initVariables();
	}
	
	@Override
	public boolean isTinkerRoot() {
		return false;
	}

}