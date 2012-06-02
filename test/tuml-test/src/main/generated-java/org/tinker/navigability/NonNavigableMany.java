package org.tinker.navigability;

import com.tinkerpop.blueprints.pgm.Vertex;

import java.util.UUID;

import org.tinker.concretetest.God;
import org.tinker.concretetest.Universe;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.collection.TinkerMultiplicityImpl;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TinkerSetImpl;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.CompositionNode;

public class NonNavigableMany extends BaseTinker implements CompositionNode {
	private TinkerSet<God> god;
	private TinkerSet<String> name;
	private TinkerSet<Universe> universe;

	/** Constructor for NonNavigableMany
	 * 
	 * @param compositeOwner 
	 */
	public NonNavigableMany(God compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		createComponents();
		init(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/** Constructor for NonNavigableMany
	 * 
	 * @param vertex 
	 */
	public NonNavigableMany(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/** Default constructor for NonNavigableMany
	 */
	public NonNavigableMany() {
	}
	
	/** Constructor for NonNavigableMany
	 * 
	 * @param persistent 
	 */
	public NonNavigableMany(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
		initialiseProperties();
	}

	public void addToGod(God god) {
		if ( god != null ) {
			god.z_internalRemoveFromNonNavigableMany(god.getNonNavigableMany());
			god.z_internalAddToNonNavigableMany(this);
			z_internalAddToGod(god);
		}
	}
	
	public void addToName(String name) {
		if ( name != null ) {
			z_internalAddToName(name);
		}
	}
	
	public void addToUniverse(Universe universe) {
		if ( universe != null ) {
			universe.z_internalRemoveFromNonNavigableMany(universe.getNonNavigableMany());
			universe.z_internalAddToNonNavigableMany(this);
			z_internalAddToUniverse(universe);
		}
	}
	
	public void createComponents() {
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
	public Long getId() {
		return TinkerIdUtilFactory.getIdUtil().getId(this.vertex);
	}
	
	public String getName() {
		TinkerSet<String> tmp = this.name;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
	}
	
	@Override
	public int getObjectVersion() {
		return TinkerIdUtilFactory.getIdUtil().getVersion(this.vertex);
	}
	
	@Override
	public CompositionNode getOwningObject() {
		return getGod();
	}
	
	@Override
	public String getUid() {
		String uid = (String) this.vertex.getProperty("uid");
		if ( uid==null || uid.trim().length()==0 ) {
			uid=UUID.randomUUID().toString();
			this.vertex.setProperty("uid", uid);
		}
		return uid;
	}
	
	public Universe getUniverse() {
		TinkerSet<Universe> tmp = this.universe;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
	}
	
	@Override
	public void init(CompositionNode compositeOwner) {
		this.z_internalAddToGod((God)compositeOwner);
		this.hasInitBeenCalled = true;
		initVariables();
	}
	
	public void initVariables() {
	}
	
	@Override
	public void initialiseProperties() {
		this.name =  new TinkerSetImpl<String>(this, "org__tinker__navigability__NonNavigableMany__name", true, new TinkerMultiplicityImpl(false,false,true,false,1,1), false);
	}
	
	@Override
	public boolean isTinkerRoot() {
		return false;
	}
	
	public void setGod(TinkerSet<God> god) {
	}
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}
	
	public void setName(TinkerSet<String> name) {
	}
	
	public void setUniverse(TinkerSet<Universe> universe) {
	}
	
	public void z_internalAddToGod(God god) {
		this.god.add(god);
	}
	
	public void z_internalAddToName(String name) {
		this.name.add(name);
	}
	
	public void z_internalAddToUniverse(Universe universe) {
		this.universe.add(universe);
	}
	
	public void z_internalRemoveFromGod(God god) {
		this.god.remove(god);
	}
	
	public void z_internalRemoveFromName(String name) {
		this.name.remove(name);
	}
	
	public void z_internalRemoveFromUniverse(Universe universe) {
		this.universe.remove(universe);
	}

}