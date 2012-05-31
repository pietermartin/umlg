package org.tinker.hierarchytest;

import com.tinkerpop.blueprints.pgm.Vertex;

import org.tinker.concretetest.God;
import org.tuml.runtime.domain.CompositionNode;

public class RealRootFolder extends AbstractRootFolder implements CompositionNode {


	/** Constructor for RealRootFolder
	 * 
	 * @param compositeOwner 
	 */
	public RealRootFolder(God compositeOwner) {
		super(true);
		init(compositeOwner);
	}
	
	/** Constructor for RealRootFolder
	 * 
	 * @param vertex 
	 */
	public RealRootFolder(Vertex vertex) {
		super(vertex);
	}
	
	/** Default constructor for RealRootFolder
	 */
	public RealRootFolder() {
		super.initVariables();
	}
	
	/** Constructor for RealRootFolder
	 * 
	 * @param persistent 
	 */
	public RealRootFolder(Boolean persistent) {
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