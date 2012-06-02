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
		initialiseProperties();
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