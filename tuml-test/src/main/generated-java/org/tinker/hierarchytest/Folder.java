package org.tinker.hierarchytest;

import com.tinkerpop.blueprints.pgm.Vertex;

import org.tuml.runtime.domain.CompositionNode;

public class Folder extends AbstractFolder implements CompositionNode {


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
	
	@Override
	public boolean isTinkerRoot() {
		return true;
	}

}