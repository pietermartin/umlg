package org.tinker.collectiontest;

import com.tinkerpop.blueprints.pgm.Vertex;

import java.util.UUID;

import org.tinker.concretetest.God;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.collection.TumlRuntimePropertyImpl;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TinkerSetImpl;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.CompositionNode;

public class Nightmare extends BaseTinker implements CompositionNode {
	private TinkerSet<God> god;
	private TinkerSet<God> godOfMemory;
	private TinkerSet<String> name;
	private TinkerSet<String> nameNonUnique;

	/** Constructor for Nightmare
	 * 
	 * @param compositeOwner 
	 */
	public Nightmare(God compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		createComponents();
		init(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/** Constructor for Nightmare
	 * 
	 * @param vertex 
	 */
	public Nightmare(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/** Default constructor for Nightmare
	 */
	public Nightmare() {
	}
	
	/** Constructor for Nightmare
	 * 
	 * @param persistent 
	 */
	public Nightmare(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
		initialiseProperties();
	}

	public void addToGod(God god) {
		if ( god != null ) {
			god.z_internalRemoveFromNightmare(god.getNightmare());
			god.z_internalAddToNightmare(this);
			z_internalAddToGod(god);
		}
	}
	
	public void addToGodOfMemory(God godOfMemory) {
		if ( godOfMemory != null ) {
			godOfMemory.z_internalRemoveFromMemory(godOfMemory.getMemory());
			godOfMemory.z_internalAddToMemory(this);
			z_internalAddToGodOfMemory(godOfMemory);
		}
	}
	
	public void addToName(String name) {
		if ( name != null ) {
			z_internalAddToName(name);
		}
	}
	
	public void addToNameNonUnique(String nameNonUnique) {
		if ( nameNonUnique != null ) {
			z_internalAddToNameNonUnique(nameNonUnique);
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
	
	public God getGodOfMemory() {
		TinkerSet<God> tmp = this.godOfMemory;
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
	
	public String getNameNonUnique() {
		TinkerSet<String> tmp = this.nameNonUnique;
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
		this.name =  new TinkerSetImpl<String>(this, "org__tinker__collectiontest__Nightmare__name", true, new TumlRuntimePropertyImpl(false,false,true,false,1,1), false);
		this.nameNonUnique =  new TinkerSetImpl<String>(this, "org__tinker__collectiontest__Nightmare__nameNonUnique", true, new TumlRuntimePropertyImpl(false,false,true,false,1,1), false);
	}
	
	@Override
	public boolean isTinkerRoot() {
		return false;
	}
	
	public void setGod(TinkerSet<God> god) {
	}
	
	public void setGodOfMemory(TinkerSet<God> godOfMemory) {
	}
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}
	
	public void setName(TinkerSet<String> name) {
	}
	
	public void setNameNonUnique(TinkerSet<String> nameNonUnique) {
	}
	
	public void z_internalAddToGod(God god) {
		this.god.add(god);
	}
	
	public void z_internalAddToGodOfMemory(God godOfMemory) {
		this.godOfMemory.add(godOfMemory);
	}
	
	public void z_internalAddToName(String name) {
		this.name.add(name);
	}
	
	public void z_internalAddToNameNonUnique(String nameNonUnique) {
		this.nameNonUnique.add(nameNonUnique);
	}
	
	public void z_internalRemoveFromGod(God god) {
		this.god.remove(god);
	}
	
	public void z_internalRemoveFromGodOfMemory(God godOfMemory) {
		this.godOfMemory.remove(godOfMemory);
	}
	
	public void z_internalRemoveFromName(String name) {
		this.name.remove(name);
	}
	
	public void z_internalRemoveFromNameNonUnique(String nameNonUnique) {
		this.nameNonUnique.remove(nameNonUnique);
	}

}