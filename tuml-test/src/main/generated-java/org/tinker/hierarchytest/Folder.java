package org.tinker.hierarchytest;

import com.tinkerpop.blueprints.pgm.Vertex;

import org.tuml.runtime.domain.TinkerCompositionNode;

public class Folder extends AbstractFolder implements TinkerCompositionNode {


	/** Constructor for Folder
	 * 
	 * @param vertex 
	 */
	public Folder(Vertex vertex) {
		super(vertex);
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

}