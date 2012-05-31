package org.tinker.inheritencetest;

import com.tinkerpop.blueprints.pgm.Vertex;

import org.tuml.runtime.domain.CompositionNode;

public class Biped extends Mamal implements CompositionNode {


	/** Constructor for Biped
	 * 
	 * @param vertex 
	 */
	public Biped(Vertex vertex) {
		super(vertex);
	}
	
	/** Constructor for Biped
	 * 
	 * @param persistent 
	 */
	public Biped(Boolean persistent) {
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