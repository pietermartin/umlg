package org.tinker.inheritencetest;

import com.tinkerpop.blueprints.pgm.Vertex;

import org.tuml.runtime.domain.TinkerCompositionNode;

public class Biped extends Mamal implements TinkerCompositionNode {


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

}