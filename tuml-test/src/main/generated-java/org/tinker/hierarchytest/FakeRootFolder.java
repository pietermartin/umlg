package org.tinker.hierarchytest;

import com.tinkerpop.blueprints.pgm.Vertex;

import org.tinker.concretetest.God;
import org.tuml.runtime.domain.CompositionNode;

public class FakeRootFolder extends AbstractRootFolder implements CompositionNode {


	/** Constructor for FakeRootFolder
	 * 
	 * @param compositeOwner 
	 */
	public FakeRootFolder(God compositeOwner) {
		super(true);
		init(compositeOwner);
	}
	
	/** Constructor for FakeRootFolder
	 * 
	 * @param vertex 
	 */
	public FakeRootFolder(Vertex vertex) {
		super(vertex);
	}
	
	/** Default constructor for FakeRootFolder
	 */
	public FakeRootFolder() {
		super.initVariables();
	}
	
	/** Constructor for FakeRootFolder
	 * 
	 * @param persistent 
	 */
	public FakeRootFolder(Boolean persistent) {
		super( persistent );
	}

	@Override
	public void clearCache() {
		super.clearCache();
	}
	
	public void createComponents() {
		super.createComponents();
	}
	
	public void init(God compositeOwner) {
		this.z_internalAddToGod(owner);
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