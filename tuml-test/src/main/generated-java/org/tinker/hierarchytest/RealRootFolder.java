package org.tinker.hierarchytest;

import com.tinkerpop.blueprints.pgm.Vertex;

import org.tuml.runtime.domain.CompositionNode;

public class RealRootFolder extends AbstractRootFolder implements CompositionNode {


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
	
	@Override
	public boolean isTinkerRoot() {
		return true;
	}

}