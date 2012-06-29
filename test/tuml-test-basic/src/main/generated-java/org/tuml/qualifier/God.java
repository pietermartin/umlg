package org.tuml.qualifier;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.collection.Multiplicity;
import org.tuml.runtime.collection.Qualifier;
import org.tuml.runtime.collection.TinkerQualifiedSet;
import org.tuml.runtime.collection.TinkerQualifiedSetImpl;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TinkerSetImpl;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.TinkerNode;

public class God extends BaseTinker implements TinkerNode {
	private TinkerQualifiedSet<Nature> nature;
	private TinkerSet<String> name;

	/** Constructor for God
	 * 
	 * @param vertex 
	 */
	public God(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/** Default constructor for God
	 */
	public God() {
	}
	
	/** Constructor for God
	 * 
	 * @param persistent 
	 */
	public God(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		defaultCreate();
		initialiseProperties();
		createComponents();
		Edge edge = GraphDb.getDb().addEdge(null, GraphDb.getDb().getRoot(), this.vertex, "root");
		edge.setProperty("inClass", this.getClass().getName());
	}

	public void addToName(String name) {
		if ( name != null ) {
			this.name.add(name);
		}
	}
	
	public void addToNature(Nature nature) {
		if ( nature != null ) {
			this.nature.add(nature, getQualifierForNature(nature));
		}
	}
	
	public void addToNature(Set<Nature> nature) {
		for ( Nature n : nature ) {
			this.addToNature(n);
		}
	}
	
	public void clearName() {
		this.name.clear();
	}
	
	public void clearNature() {
		this.nature.clear();
	}
	
	public void createComponents() {
	}
	
	@Override
	public void delete() {
		for ( Nature child : getNature() ) {
			child.delete();
		}
		GraphDb.getDb().removeVertex(this.vertex);
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
	
	public TinkerQualifiedSet<Nature> getNature() {
		return this.nature;
	}
	
	@Override
	public int getObjectVersion() {
		return TinkerIdUtilFactory.getIdUtil().getVersion(this.vertex);
	}
	
	public List<Qualifier> getQualifierForNature(Nature context) {
		List<Qualifier> result = new ArrayList<Qualifier>();
		result.add(new Qualifier("natureQualifier1", context.getNatureQualifier1(), Multiplicity.ONE_TO_ONE));
		return result;
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
		this.name =  new TinkerSetImpl<String>(this, GodRuntimePropertyEnum.name);
		this.nature =  new TinkerQualifiedSetImpl<Nature>(this, getUid(), GodRuntimePropertyEnum.nature);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (GodRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case nature:
				this.nature =  new TinkerQualifiedSetImpl<Nature>(this, getUid(), GodRuntimePropertyEnum.nature);
			break;
		
			case name:
				this.name =  new TinkerSetImpl<String>(this, GodRuntimePropertyEnum.name);
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
	
	public void removeFromNature(Nature nature) {
		if ( nature != null ) {
			this.nature.remove(nature);
		}
	}
	
	public void removeFromNature(Set<Nature> nature) {
		if ( !nature.isEmpty() ) {
			this.nature.removeAll(nature);
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
	
	public void setNature(Set<Nature> nature) {
		clearNature();
		addToNature(nature);
	}

	public enum GodRuntimePropertyEnum implements TumlRuntimeProperty {
		name(true,true,false,"tuml-test-basic-model__org__tuml__qualifier__God__name",false,false,true,false,1,1),
		nature(false,true,true,"A_<god>_<nature>",false,true,false,false,-1,0);
		private boolean onePrimitive;
		private boolean controllingSide;
		private boolean composite;
		private String label;
		private boolean oneToOne;
		private boolean oneToMany;
		private boolean manyToOne;
		private boolean manyToMany;
		private int upper;
		private int lower;
		/** Constructor for GodRuntimePropertyEnum
		 * 
		 * @param onePrimitive 
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
		private GodRuntimePropertyEnum(boolean onePrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower) {
			this.onePrimitive = onePrimitive;
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
	
		static public GodRuntimePropertyEnum fromLabel(String label) {
			if ( name.getLabel().equals(label) ) {
				return name;
			}
			if ( nature.getLabel().equals(label) ) {
				return nature;
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
		
		public boolean isOnePrimitive() {
			return this.onePrimitive;
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