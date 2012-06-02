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
		initialiseProperties();
	}
	
	/** Default constructor for Biped
	 */
	public Biped() {
		super.initVariables();
	}
	
	/** Constructor for Biped
	 * 
	 * @param persistent 
	 */
	public Biped(Boolean persistent) {
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