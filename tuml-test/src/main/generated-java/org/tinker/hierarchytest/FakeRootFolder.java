package org.tinker.hierarchytest;

import com.tinkerpop.blueprints.pgm.Vertex;

import org.tuml.runtime.domain.CompositionNode;

public class FakeRootFolder extends AbstractRootFolder implements CompositionNode {


	/** Constructor for FakeRootFolder
	 * 
	 * @param vertex 
	 */
	public FakeRootFolder(Vertex vertex) {
		super(vertex);
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
	
	@Override
	public boolean isTinkerRoot() {
		return true;
	}

}