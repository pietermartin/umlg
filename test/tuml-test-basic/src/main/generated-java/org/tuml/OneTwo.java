package org.tuml;

import com.tinkerpop.blueprints.pgm.Vertex;

import java.util.Set;
import java.util.UUID;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TinkerSetImpl;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.TinkerNode;

public class OneTwo extends BaseTinker implements TinkerNode {
	private TinkerSet<String> name;
	private TinkerSet<OneOne> oneOne;

	/** Constructor for OneTwo
	 * 
	 * @param vertex 
	 */
	public OneTwo(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/** Default constructor for OneTwo
	 */
	public OneTwo() {
	}
	
	/** Constructor for OneTwo
	 * 
	 * @param persistent 
	 */
	public OneTwo(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		defaultCreate();
		initialiseProperties();
	}

	public void addToName(String name) {
		if ( name != null ) {
			this.name.add(name);
		}
	}
	
	public void addToOneOne(OneOne oneOne) {
		if ( oneOne != null ) {
			this.oneOne.add(oneOne);
		}
	}
	
	public void clearName() {
		this.name.clear();
	}
	
	public void clearOneOne() {
		this.oneOne.clear();
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
	
	public OneOne getOneOne() {
		TinkerSet<OneOne> tmp = this.oneOne;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
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
	
	public void initVariables() {
	}
	
	@Override
	public void initialiseProperties() {
		this.oneOne =  new TinkerSetImpl<OneOne>(this, OneTwoRuntimePropertyEnum.ONEONE);
		this.name =  new TinkerSetImpl<String>(this, OneTwoRuntimePropertyEnum.NAME);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (OneTwoRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case NAME:
				this.name =  new TinkerSetImpl<String>(this, OneTwoRuntimePropertyEnum.NAME);
			break;
		
			case ONEONE:
				this.oneOne =  new TinkerSetImpl<OneOne>(this, OneTwoRuntimePropertyEnum.ONEONE);
			break;
		
		}
	}
	
	@Override
	public boolean isTinkerRoot() {
		return true;
	}
	
	public void removeFromName(Set<String> name) {
		if ( !name.isEmpty() ) {
			this.name.removeAll(name);
		}
	}
	
	public void removeFromName(String name) {
		if ( name != null ) {
			this.name.remove(name);
		}
	}
	
	public void removeFromOneOne(OneOne oneOne) {
		if ( oneOne != null ) {
			this.oneOne.remove(oneOne);
		}
	}
	
	public void removeFromOneOne(Set<OneOne> oneOne) {
		if ( !oneOne.isEmpty() ) {
			this.oneOne.removeAll(oneOne);
		}
	}
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}
	
	public void setName(String name) {
		clearName();
		addToName(name);
	}
	
	public void setOneOne(OneOne oneOne) {
		clearOneOne();
		addToOneOne(oneOne);
	}

	public enum OneTwoRuntimePropertyEnum implements TumlRuntimeProperty {
		ONEONE(true,false,"A_<oneOne>_<oneTwo>",true,false,false,false,1,1),
		NAME(true,false,"org__tuml__OneTwo__name",false,false,true,false,1,1);
		private boolean controllingSide;
		private boolean composite;
		private String label;
		private boolean oneToOne;
		private boolean oneToMany;
		private boolean manyToOne;
		private boolean manyToMany;
		private int upper;
		private int lower;
		/** Constructor for OneTwoRuntimePropertyEnum
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
		private OneTwoRuntimePropertyEnum(boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower) {
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
	
		static public OneTwoRuntimePropertyEnum fromLabel(String label) {
			if ( ONEONE.getLabel().equals(label) ) {
				return ONEONE;
			}
			if ( NAME.getLabel().equals(label) ) {
				return NAME;
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
			return (getUpper() == -1 || elementCount <= getUpper()) && elementCount >= getLower();
		}
	
	}
}