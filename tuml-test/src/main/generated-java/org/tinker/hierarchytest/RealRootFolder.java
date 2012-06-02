package org.tinker.hierarchytest;

import com.tinkerpop.blueprints.pgm.Vertex;

import org.tinker.concretetest.God;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.domain.CompositionNode;

public class RealRootFolder extends AbstractRootFolder implements CompositionNode {
	private TinkerSet<God> god;

	/** Constructor for RealRootFolder
	 * 
	 * @param compositeOwner 
	 */
	public RealRootFolder(God compositeOwner) {
		super(true);
		init(compositeOwner);
	}
	
	/** Constructor for RealRootFolder
	 * 
	 * @param vertex 
	 */
	public RealRootFolder(Vertex vertex) {
		super(vertex);
		initialiseProperties();
	}
	
	/** Default constructor for RealRootFolder
	 */
	public RealRootFolder() {
		super.initVariables();
	}
	
	/** Constructor for RealRootFolder
	 * 
	 * @param persistent 
	 */
	public RealRootFolder(Boolean persistent) {
		super( persistent );
		initialiseProperties();
	}

	public void addToGod(God god) {
		if ( god != null ) {
			god.z_internalRemoveFromRealRootFolder(god.getRealRootFolder());
			god.z_internalAddToRealRootFolder(this);
			z_internalAddToGod(god);
		}
	}
	
	public void createComponents() {
		super.createComponents();
	}
	
	@Override
	public void delete() {
	}
	
	public God getGod() {
		TinkerSet<God> tmp = this.god;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
	}
	
	@Override
	public CompositionNode getOwningObject() {
		return getGod();
	}
	
	@Override
	public void init(CompositionNode compositeOwner) {
		this.z_internalAddToGod((God)compositeOwner);
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
		return false;
	}
	
	public void setGod(TinkerSet<God> god) {
	}
	
	public void z_internalAddToGod(God god) {
		this.god.add(god);
	}
	
	public void z_internalRemoveFromGod(God god) {
		this.god.remove(god);
	}

}