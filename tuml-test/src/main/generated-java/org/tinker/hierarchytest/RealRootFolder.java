package org.tinker.hierarchytest;

import com.tinkerpop.blueprints.pgm.Vertex;

import org.tuml.runtime.domain.TinkerCompositionNode;

public class RealRootFolder extends AbstractRootFolder implements TinkerCompositionNode {


	/** Constructor for RealRootFolder
	 * 
	 * @param vertex 
	 */
	public RealRootFolder(Vertex vertex) {
		super(vertex);
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

}