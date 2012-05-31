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
	
	/** Default constructor for Quadped
	 */
	public Quadped() {
		super.initVariables();
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