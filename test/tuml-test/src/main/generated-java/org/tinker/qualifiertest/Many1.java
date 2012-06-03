package org.tinker.qualifiertest;

import com.tinkerpop.blueprints.pgm.Vertex;

import java.util.UUID;

import org.tinker.concretetest.God;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.collection.TumlRuntimePropertyImpl;
import org.tuml.runtime.collection.TinkerQualifiedSequence;
import org.tuml.runtime.collection.TinkerQualifiedSet;
import org.tuml.runtime.collection.TinkerSequence;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TinkerSetImpl;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.CompositionNode;

public class Many1 extends BaseTinker implements CompositionNode {
	private TinkerSet<God> god;
	private TinkerQualifiedSet<Many2> many2;
	private TinkerQualifiedSequence<Many2> many2List;
	private TinkerSequence<Many2> many2UnqualifiedList;
	private TinkerSet<String> name;

	/** Constructor for Many1
	 * 
	 * @param compositeOwner 
	 */
	public Many1(God compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		createComponents();
		init(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/** Constructor for Many1
	 * 
	 * @param vertex 
	 */
	public Many1(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/** Default constructor for Many1
	 */
	public Many1() {
	}
	
	/** Constructor for Many1
	 * 
	 * @param persistent 
	 */
	public Many1(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
		initialiseProperties();
	}

	public void addToGod(God god) {
		if ( god != null ) {
			god.z_internalRemoveFromMany1(god.getMany1());
			god.z_internalAddToMany1(this);
			z_internalAddToGod(god);
		}
	}
	
	public void addToMany2(Many2 many2) {
		if ( many2 != null ) {
			many2.z_internalRemoveFromMany1(many2.getMany1());
			many2.z_internalAddToMany1(this);
			z_internalAddToMany2(many2);
		}
	}
	
	public void addToMany2List(Many2 many2List) {
		if ( many2List != null ) {
			many2List.z_internalRemoveFromMany1List(many2List.getMany1List());
			many2List.z_internalAddToMany1List(this);
			z_internalAddToMany2List(many2List);
		}
	}
	
	public void addToMany2UnqualifiedList(Many2 many2UnqualifiedList) {
		if ( many2UnqualifiedList != null ) {
			many2UnqualifiedList.z_internalRemoveFromMany1UnqualifiedList(many2UnqualifiedList.getMany1UnqualifiedList());
			many2UnqualifiedList.z_internalAddToMany1UnqualifiedList(this);
			z_internalAddToMany2UnqualifiedList(many2UnqualifiedList);
		}
	}
	
	public void addToName(String name) {
		if ( name != null ) {
			z_internalAddToName(name);
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
	
	public TinkerQualifiedSet<Many2> getMany2() {
		return this.many2;
	}
	
	public TinkerQualifiedSequence<Many2> getMany2List() {
		return this.many2List;
	}
	
	public TinkerSequence<Many2> getMany2UnqualifiedList() {
		return this.many2UnqualifiedList;
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
		this.name =  new TinkerSetImpl<String>(this, "org__tinker__qualifiertest__Many1__name", true, new TumlRuntimePropertyImpl(false,false,true,false,1,1), false);
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
	
	public void z_internalAddToGod(God god) {
		this.god.add(god);
	}
	
	public void z_internalAddToMany2(Many2 many2) {
		this.many2.add(many2);
	}
	
	public void z_internalAddToMany2List(Many2 many2List) {
		this.many2List.add(many2List);
	}
	
	public void z_internalAddToMany2UnqualifiedList(Many2 many2UnqualifiedList) {
		this.many2UnqualifiedList.add(many2UnqualifiedList);
	}
	
	public void z_internalAddToName(String name) {
		this.name.add(name);
	}
	
	public void z_internalRemoveFromGod(God god) {
		this.god.remove(god);
	}
	
	public void z_internalRemoveFromMany2(Many2 many2) {
		this.many2.remove(many2);
	}
	
	public void z_internalRemoveFromMany2List(Many2 many2List) {
		this.many2List.remove(many2List);
	}
	
	public void z_internalRemoveFromMany2UnqualifiedList(Many2 many2UnqualifiedList) {
		this.many2UnqualifiedList.remove(many2UnqualifiedList);
	}
	
	public void z_internalRemoveFromName(String name) {
		this.name.remove(name);
	}

}