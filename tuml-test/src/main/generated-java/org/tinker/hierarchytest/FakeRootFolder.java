package org.tinker.hierarchytest;

import com.tinkerpop.blueprints.pgm.Vertex;

import org.tuml.runtime.domain.TinkerCompositionNode;

public class FakeRootFolder extends AbstractRootFolder implements TinkerCompositionNode {


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

}