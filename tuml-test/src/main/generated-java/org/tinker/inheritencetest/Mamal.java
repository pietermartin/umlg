package org.tinker.inheritencetest;

import com.tinkerpop.blueprints.pgm.Vertex;

import org.tuml.runtime.domain.CompositionNode;

public class Mamal extends AbstractSpecies implements CompositionNode {


	/** Constructor for Mamal
	 * 
	 * @param vertex 
	 */
	public Mamal(Vertex vertex) {
		super(vertex);
	}
	
	/** Default constructor for Mamal
	 */
	public Mamal() {
		super.initVariables();
	}
	
	/** Constructor for Mamal
	 * 
	 * @param persistent 
	 */
	public Mamal(Boolean persistent) {
		super( persistent );
	}

	@Override
	public void clearCache() {
		super.clearCache();
	}
	
	public void createComponents() {
		super.createComponents();
	}
	
	public void init() {
		this.hasInitBeenCalled = true;
		initVariables();
	}
	
	public void initVariables() {
		super.initVariables();
	}
	
	@Override
	public boolean isTinkerRoot() {
		return true;
	}

}