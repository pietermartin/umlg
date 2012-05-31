package org.tinker.inheritencetest;

import com.tinkerpop.blueprints.pgm.Vertex;

import org.tuml.runtime.domain.CompositionNode;

public class Quadped extends Mamal implements CompositionNode {


	/** Constructor for Quadped
	 * 
	 * @param vertex 
	 */
	public Quadped(Vertex vertex) {
		super(vertex);
	}
	
	/** Constructor for Quadped
	 * 
	 * @param persistent 
	 */
	public Quadped(Boolean persistent) {
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