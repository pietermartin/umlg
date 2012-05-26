package org.tinker.hierarchytest;

import com.tinkerpop.blueprints.pgm.Vertex;

import org.tuml.runtime.domain.TinkerCompositionNode;

public class AbstractRootFolder extends AbstractFolder implements TinkerCompositionNode {


	/** Constructor for AbstractRootFolder
	 * 
	 * @param vertex 
	 */
	public AbstractRootFolder(Vertex vertex) {
		super(vertex);
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

}