package org.tinker.hierarchytest;

import com.tinkerpop.blueprints.pgm.Vertex;

import org.tuml.runtime.domain.CompositionNode;

public class AbstractRootFolder extends AbstractFolder implements CompositionNode {


	/** Constructor for AbstractRootFolder
	 * 
	 * @param vertex 
	 */
	public AbstractRootFolder(Vertex vertex) {
		super(vertex);
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
	}

	@Override
	public void clearCache() {
		super.clearCache();
	}
	
	public void createComponents() {
		super.createComponents();
	}
	
	public void init() {
		this.hasInitBeenCalled = true;
		initVariables();
	}
	
	public void initVariables() {
		super.initVariables();
	}
	
	@Override
	public boolean isTinkerRoot() {
		return true;
	}

}