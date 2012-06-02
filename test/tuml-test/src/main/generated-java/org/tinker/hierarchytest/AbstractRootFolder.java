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
		initialiseProperties();
	}

	public void createComponents() {
		super.createComponents();
	}
	
	@Override
	public void delete() {
	}
	
	@Override
	public CompositionNode getOwningObject() {
		return null;
	}
	
	@Override
	public void init(CompositionNode compositeOwner) {
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
		return true;
	}

}