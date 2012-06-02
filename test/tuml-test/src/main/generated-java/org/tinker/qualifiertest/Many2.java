package org.tinker.qualifiertest;

import com.tinkerpop.blueprints.pgm.Vertex;

import java.util.UUID;

import org.tinker.concretetest.God;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.collection.TinkerMultiplicityImpl;
import org.tuml.runtime.collection.TinkerQualifiedSequence;
import org.tuml.runtime.collection.TinkerQualifiedSet;
import org.tuml.runtime.collection.TinkerSequence;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TinkerSetImpl;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.CompositionNode;

public class Many2 extends BaseTinker implements CompositionNode {
	private TinkerSet<God> god;
	private TinkerQualifiedSet<Many1> many1;
	private TinkerQualifiedSequence<Many1> many1List;
	private TinkerSequence<Many1> many1UnqualifiedList;
	private TinkerSet<String> name;

	/** Constructor for Many2
	 * 
	 * @param compositeOwner 
	 */
	public Many2(God compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		createComponents();
		init(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/** Constructor for Many2
	 * 
	 * @param vertex 
	 */
	public Many2(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/** Default constructor for Many2
	 */
	public Many2() {
	}
	
	/** Constructor for Many2
	 * 
	 * @param persistent 
	 */
	public Many2(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
		initialiseProperties();
	}

	public void addToGod(God god) {
		if ( god != null ) {
			god.z_internalRemoveFromMany2(god.getMany2());
			god.z_internalAddToMany2(this);
			z_internalAddToGod(god);
		}
	}
	
	public void addToMany1(Many1 many1) {
		if ( many1 != null ) {
			many1.z_internalRemoveFromMany2(many1.getMany2());
			many1.z_internalAddToMany2(this);
			z_internalAddToMany1(many1);
		}
	}
	
	public void addToMany1List(Many1 many1List) {
		if ( many1List != null ) {
			many1List.z_internalRemoveFromMany2List(many1List.getMany2List());
			many1List.z_internalAddToMany2List(this);
			z_internalAddToMany1List(many1List);
		}
	}
	
	public void addToMany1UnqualifiedList(Many1 many1UnqualifiedList) {
		if ( many1UnqualifiedList != null ) {
			many1UnqualifiedList.z_internalRemoveFromMany2UnqualifiedList(many1UnqualifiedList.getMany2UnqualifiedList());
			many1UnqualifiedList.z_internalAddToMany2UnqualifiedList(this);
			z_internalAddToMany1UnqualifiedList(many1UnqualifiedList);
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
	
	public TinkerQualifiedSet<Many1> getMany1() {
		return this.many1;
	}
	
	public TinkerQualifiedSequence<Many1> getMany1List() {
		return this.many1List;
	}
	
	public TinkerSequence<Many1> getMany1UnqualifiedList() {
		return this.many1UnqualifiedList;
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
		this.name =  new TinkerSetImpl<String>(this, "org__tinker__qualifiertest__Many2__name", true, new TinkerMultiplicityImpl(false,false,true,false,1,1), false);
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
	
	public void z_internalAddToMany1(Many1 many1) {
		this.many1.add(many1);
	}
	
	public void z_internalAddToMany1List(Many1 many1List) {
		this.many1List.add(many1List);
	}
	
	public void z_internalAddToMany1UnqualifiedList(Many1 many1UnqualifiedList) {
		this.many1UnqualifiedList.add(many1UnqualifiedList);
	}
	
	public void z_internalAddToName(String name) {
		this.name.add(name);
	}
	
	public void z_internalRemoveFromGod(God god) {
		this.god.remove(god);
	}
	
	public void z_internalRemoveFromMany1(Many1 many1) {
		this.many1.remove(many1);
	}
	
	public void z_internalRemoveFromMany1List(Many1 many1List) {
		this.many1List.remove(many1List);
	}
	
	public void z_internalRemoveFromMany1UnqualifiedList(Many1 many1UnqualifiedList) {
		this.many1UnqualifiedList.remove(many1UnqualifiedList);
	}
	
	public void z_internalRemoveFromName(String name) {
		this.name.remove(name);
	}

}