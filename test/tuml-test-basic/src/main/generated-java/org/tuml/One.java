package org.tuml;

import com.tinkerpop.blueprints.pgm.Vertex;

import java.util.Set;
import java.util.UUID;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TinkerSetImpl;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.CompositionNode;

public class One extends BaseTinker implements CompositionNode {
	private TinkerSet<String> name;
	private TinkerSet<Many> many;

	/** Constructor for One
	 * 
	 * @param vertex 
	 */
	public One(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/** Default constructor for One
	 */
	public One() {
	}
	
	/** Constructor for One
	 * 
	 * @param persistent 
	 */
	public One(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
		initialiseProperties();
	}

	public void addToMany(Many many) {
		if ( many != null ) {
			many.z_internalRemoveFromOne(many.getOne());
			many.z_internalAddToOne(this);
			z_internalAddToMany(many);
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
	
	@Override
	public Long getId() {
		return TinkerIdUtilFactory.getIdUtil().getId(this.vertex);
	}
	
	public TinkerSet<Many> getMany() {
		return this.many;
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
		this.name =  new TinkerSetImpl<String>(this, OneRuntimePropertyEnum.NAME);
		this.many =  new TinkerSetImpl<Many>(this, OneRuntimePropertyEnum.MANY);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (OneRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case MANY:
				this.many =  new TinkerSetImpl<Many>(this, OneRuntimePropertyEnum.MANY);
			break;
		
			case NAME:
				this.name =  new TinkerSetImpl<String>(this, OneRuntimePropertyEnum.NAME);
			break;
		
		}
	}
	
	@Override
	public boolean isTinkerRoot() {
		return true;
	}
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}
	
	public void setName(Set<String> name) {
	}
	
	public void z_internalAddToMany(Many many) {
		this.many.add(many);
	}
	
	public void z_internalAddToName(String name) {
		this.name.add(name);
	}
	
	public void z_internalRemoveFromMany(Many many) {
		this.many.remove(many);
	}
	
	public void z_internalRemoveFromName(String name) {
		this.name.remove(name);
	}

	public enum OneRuntimePropertyEnum implements TumlRuntimeProperty {
		NAME(true,false,"org__tuml__One__name",false,true,false,false,1,1),
		MANY(true,true,"A_<one>_<many>",true,false,false,false,-1,0);
		private boolean controllingSide;
		private boolean composite;
		private String label;
		private boolean oneToOne;
		private boolean oneToMany;
		private boolean manyToOne;
		private boolean manyToMany;
		private int upper;
		private int lower;
		/** Constructor for OneRuntimePropertyEnum
		 * 
		 * @param controllingSide 
		 * @param composite 
		 * @param label 
		 * @param oneToOne 
		 * @param oneToMany 
		 * @param manyToOne 
		 * @param manyToMany 
		 * @param upper 
		 * @param lower 
		 */
		private OneRuntimePropertyEnum(boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower) {
			this.controllingSide = controllingSide;
			this.composite = composite;
			this.label = label;
			this.oneToOne = oneToOne;
			this.oneToMany = oneToMany;
			this.manyToOne = manyToOne;
			this.manyToMany = manyToMany;
			this.upper = upper;
			this.lower = lower;
		}
	
		static public OneRuntimePropertyEnum fromLabel(String label) {
			if ( NAME.getLabel().equals(label) ) {
				return NAME;
			}
			if ( MANY.getLabel().equals(label) ) {
				return MANY;
			}
			throw new IllegalStateException();
		}
		
		public String getLabel() {
			return this.label;
		}
		
		public int getLower() {
			return this.lower;
		}
		
		public int getUpper() {
			return this.upper;
		}
		
		public boolean isComposite() {
			return this.composite;
		}
		
		public boolean isControllingSide() {
			return this.controllingSide;
		}
		
		public boolean isManyToMany() {
			return this.manyToMany;
		}
		
		public boolean isManyToOne() {
			return this.manyToOne;
		}
		
		public boolean isOneToMany() {
			return this.oneToMany;
		}
		
		public boolean isOneToOne() {
			return this.oneToOne;
		}
		
		@Override
		public boolean isValid(int elementCount) {
			return elementCount <= getUpper() && elementCount >= getLower();
		}
	
	}
}