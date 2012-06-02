package org.tinker.hierarchytest;

import com.tinkerpop.blueprints.pgm.Vertex;

import java.util.UUID;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.collection.TinkerMultiplicityImpl;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TinkerSetImpl;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.CompositionNode;

public class AbstractFolder extends BaseTinker implements CompositionNode {
	private TinkerSet<Folder> childFolder;
	private TinkerSet<String> name;

	/** Constructor for AbstractFolder
	 * 
	 * @param vertex 
	 */
	public AbstractFolder(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/** Default constructor for AbstractFolder
	 */
	public AbstractFolder() {
	}
	
	/** Constructor for AbstractFolder
	 * 
	 * @param persistent 
	 */
	public AbstractFolder(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
		initialiseProperties();
	}

	public void addToChildFolder(Folder childFolder) {
		if ( childFolder != null ) {
			childFolder.z_internalRemoveFromParentFolder(childFolder.getParentFolder());
			childFolder.z_internalAddToParentFolder(this);
			z_internalAddToChildFolder(childFolder);
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
	
	public TinkerSet<Folder> getChildFolder() {
		return this.childFolder;
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
		return null;
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
		this.hasInitBeenCalled = true;
		initVariables();
	}
	
	public void initVariables() {
	}
	
	@Override
	public void initialiseProperties() {
		this.name =  new TinkerSetImpl<String>(this, "org__tinker__hierarchytest__AbstractFolder__name", true, new TinkerMultiplicityImpl(false,false,true,false,1,1), false);
		this.childFolder =  new TinkerSetImpl<Folder>(this, "A_<abstractFolder>_<folder>", true, new TinkerMultiplicityImpl(false,true,false,false,0,-1), true);
	}
	
	@Override
	public boolean isTinkerRoot() {
		return true;
	}
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}
	
	public void setName(TinkerSet<String> name) {
	}
	
	public void z_internalAddToChildFolder(Folder childFolder) {
		this.childFolder.add(childFolder);
	}
	
	public void z_internalAddToName(String name) {
		this.name.add(name);
	}
	
	public void z_internalRemoveFromChildFolder(Folder childFolder) {
		this.childFolder.remove(childFolder);
	}
	
	public void z_internalRemoveFromName(String name) {
		this.name.remove(name);
	}

}