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
		initialiseProperties();
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
		initialiseProperties();
	}

	public void createComponents() {
		super.createComponents();
	}
	
	@Override
	public void delete() {
	}
	
	@Override
	public CompositionNode getOwningObject() {
		return null;
	}
	
	@Override
	public void init(CompositionNode compositeOwner) {
		this.hasInitBeenCalled = true;
		initVariables();
	}
	
	public void initVariables() {
		super.initVariables();
	}
	
	@Override
	public void initialiseProperties() {
		super.initialiseProperties();
	}
	
	@Override
	public boolean isTinkerRoot() {
		return true;
	}

}