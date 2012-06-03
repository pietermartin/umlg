package org.tinker.qualifiertest;

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

public class Nature extends BaseTinker implements CompositionNode {
	private TinkerSet<God> god;
	private TinkerSet<String> name1;
	private TinkerSet<String> name2;

	/** Constructor for Nature
	 * 
	 * @param compositeOwner 
	 */
	public Nature(God compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		createComponents();
		init(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/** Constructor for Nature
	 * 
	 * @param vertex 
	 */
	public Nature(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/** Default constructor for Nature
	 */
	public Nature() {
	}
	
	/** Constructor for Nature
	 * 
	 * @param persistent 
	 */
	public Nature(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
		initialiseProperties();
	}

	public void addToGod(God god) {
		if ( god != null ) {
			god.z_internalRemoveFromNature(god.getNature());
			god.z_internalAddToNature(this);
			z_internalAddToGod(god);
		}
	}
	
	public void addToName1(String name1) {
		if ( name1 != null ) {
			z_internalAddToName1(name1);
		}
	}
	
	public void addToName2(String name2) {
		if ( name2 != null ) {
			z_internalAddToName2(name2);
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
	
	public String getName1() {
		TinkerSet<String> tmp = this.name1;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
	}
	
	public String getName2() {
		TinkerSet<String> tmp = this.name2;
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
		this.name1 =  new TinkerSetImpl<String>(this, "org__tinker__qualifiertest__Nature__name1", true, new TumlRuntimePropertyImpl(false,false,true,false,1,1), false);
		this.name2 =  new TinkerSetImpl<String>(this, "org__tinker__qualifiertest__Nature__name2", true, new TumlRuntimePropertyImpl(false,false,true,false,1,1), false);
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
	
	public void setName1(TinkerSet<String> name1) {
	}
	
	public void setName2(TinkerSet<String> name2) {
	}
	
	public void z_internalAddToGod(God god) {
		this.god.add(god);
	}
	
	public void z_internalAddToName1(String name1) {
		this.name1.add(name1);
	}
	
	public void z_internalAddToName2(String name2) {
		this.name2.add(name2);
	}
	
	public void z_internalRemoveFromGod(God god) {
		this.god.remove(god);
	}
	
	public void z_internalRemoveFromName1(String name1) {
		this.name1.remove(name1);
	}
	
	public void z_internalRemoveFromName2(String name2) {
		this.name2.remove(name2);
	}

}