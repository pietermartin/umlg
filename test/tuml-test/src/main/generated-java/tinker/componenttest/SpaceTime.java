package tinker.componenttest;

import com.tinkerpop.blueprints.Vertex;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.collection.Qualifier;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TinkerSetImpl;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.CompositionNode;
import org.tuml.runtime.domain.TinkerNode;

import tinker.concretetest.Universe;

public class SpaceTime extends BaseTinker implements CompositionNode {
	static final public long serialVersionUID = 1L;
	private TinkerSet<String> name;
	private TinkerSet<Space> space;
	private TinkerSet<Time> time;
	private TinkerSet<Universe> universe;

	/** Constructor for SpaceTime
	 * 
	 * @param compositeOwner 
	 */
	public SpaceTime(Universe compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		initialiseProperties();
		createComponents();
		init(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/** Constructor for SpaceTime
	 * 
	 * @param vertex 
	 */
	public SpaceTime(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/** Default constructor for SpaceTime
	 */
	public SpaceTime() {
	}
	
	/** Constructor for SpaceTime
	 * 
	 * @param persistent 
	 */
	public SpaceTime(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
		initialiseProperties();
		createComponents();
	}

	public void addToName(String name) {
		if ( name != null ) {
			this.name.add(name);
		}
	}
	
	public void addToSpace(Space space) {
		if ( space != null ) {
			this.space.add(space);
		}
	}
	
	public void addToTime(Time time) {
		if ( time != null ) {
			this.time.add(time);
		}
	}
	
	public void addToUniverse(Universe universe) {
		if ( universe != null ) {
			this.universe.add(universe);
		}
	}
	
	public void clearName() {
		this.name.clear();
	}
	
	public void clearSpace() {
		this.space.clear();
	}
	
	public void clearTime() {
		this.time.clear();
	}
	
	public void clearUniverse() {
		this.universe.clear();
	}
	
	public void createComponents() {
		if ( getSpace() == null ) {
			setSpace(new Space(true));
		}
		if ( getTime() == null ) {
			setTime(new Time(true));
		}
	}
	
	@Override
	public void delete() {
		if ( getSpace() != null ) {
			getSpace().delete();
		}
		if ( getTime() != null ) {
			getTime().delete();
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
	
	@Override
	public int getObjectVersion() {
		return TinkerIdUtilFactory.getIdUtil().getVersion(this.vertex);
	}
	
	@Override
	public TinkerNode getOwningObject() {
		return getUniverse();
	}
	
	/** GetQualifiers is called from the collection in order to update the index used to implement the qualifier
	 * 
	 * @param tumlRuntimeProperty 
	 * @param node 
	 */
	@Override
	public List<Qualifier> getQualifiers(TumlRuntimeProperty tumlRuntimeProperty, TinkerNode node) {
		List<Qualifier> result = Collections.emptyList();
		SpaceTimeRuntimePropertyEnum runtimeProperty = SpaceTimeRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result.isEmpty() ) {
			switch ( runtimeProperty ) {
				default:
					result = Collections.emptyList();
				break;
			}
		
		}
		return result;
	}
	
	/** GetSize is called from the collection in order to update the index used to implement a sequance's index
	 * 
	 * @param tumlRuntimeProperty 
	 */
	@Override
	public int getSize(TumlRuntimeProperty tumlRuntimeProperty) {
		int result = 0;
		SpaceTimeRuntimePropertyEnum runtimeProperty = SpaceTimeRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result == 0 ) {
			switch ( runtimeProperty ) {
				case universe:
					result = universe.size();
				break;
			
				case time:
					result = time.size();
				break;
			
				case name:
					result = name.size();
				break;
			
				case space:
					result = space.size();
				break;
			
				default:
					result = 0;
				break;
			}
		
		}
		return result;
	}
	
	public Space getSpace() {
		TinkerSet<Space> tmp = this.space;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
	}
	
	public Time getTime() {
		TinkerSet<Time> tmp = this.time;
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
	
	public Universe getUniverse() {
		TinkerSet<Universe> tmp = this.universe;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
	}
	
	/** This gets called on creation with the compositional owner. The composition owner does not itself need to be a composite node
	 * 
	 * @param compositeOwner 
	 */
	@Override
	public void init(TinkerNode compositeOwner) {
		this.addToUniverse((Universe)compositeOwner);
		this.hasInitBeenCalled = true;
		initVariables();
	}
	
	public void initVariables() {
	}
	
	@Override
	public void initialiseProperties() {
		this.space =  new TinkerSetImpl<Space>(this, SpaceTimeRuntimePropertyEnum.space);
		this.name =  new TinkerSetImpl<String>(this, SpaceTimeRuntimePropertyEnum.name);
		this.time =  new TinkerSetImpl<Time>(this, SpaceTimeRuntimePropertyEnum.time);
		this.universe =  new TinkerSetImpl<Universe>(this, SpaceTimeRuntimePropertyEnum.universe);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (SpaceTimeRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case universe:
				this.universe =  new TinkerSetImpl<Universe>(this, SpaceTimeRuntimePropertyEnum.universe);
			break;
		
			case time:
				this.time =  new TinkerSetImpl<Time>(this, SpaceTimeRuntimePropertyEnum.time);
			break;
		
			case name:
				this.name =  new TinkerSetImpl<String>(this, SpaceTimeRuntimePropertyEnum.name);
			break;
		
			case space:
				this.space =  new TinkerSetImpl<Space>(this, SpaceTimeRuntimePropertyEnum.space);
			break;
		
		}
	}
	
	@Override
	public boolean isTinkerRoot() {
		return false;
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
	
	public void removeFromSpace(Set<Space> space) {
		if ( !space.isEmpty() ) {
			this.space.removeAll(space);
		}
	}
	
	public void removeFromSpace(Space space) {
		if ( space != null ) {
			this.space.remove(space);
		}
	}
	
	public void removeFromTime(Set<Time> time) {
		if ( !time.isEmpty() ) {
			this.time.removeAll(time);
		}
	}
	
	public void removeFromTime(Time time) {
		if ( time != null ) {
			this.time.remove(time);
		}
	}
	
	public void removeFromUniverse(Set<Universe> universe) {
		if ( !universe.isEmpty() ) {
			this.universe.removeAll(universe);
		}
	}
	
	public void removeFromUniverse(Universe universe) {
		if ( universe != null ) {
			this.universe.remove(universe);
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
	
	public void setSpace(Space space) {
		clearSpace();
		addToSpace(space);
	}
	
	public void setTime(Time time) {
		clearTime();
		addToTime(time);
	}
	
	public void setUniverse(Universe universe) {
		clearUniverse();
		addToUniverse(universe);
	}

	public enum SpaceTimeRuntimePropertyEnum implements TumlRuntimeProperty {
		space(false,true,true,"A_<spaceTime>_<space>",true,false,false,false,1,1,false,false,false,false,true),
		name(true,true,false,"org__tinker__componenttest__SpaceTime__name",false,false,true,false,1,1,false,false,false,false,true),
		time(false,true,true,"A_<spaceTime>_<time>",true,false,false,false,1,1,false,false,false,false,true),
		universe(false,false,false,"A_<universe>_<spaceTime>",true,false,false,false,1,1,false,false,false,false,true);
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
		private boolean qualified;
		private boolean inverseQualified;
		private boolean ordered;
		private boolean inverseOrdered;
		private boolean unique;
		/** Constructor for SpaceTimeRuntimePropertyEnum
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
		 * @param qualified 
		 * @param inverseQualified 
		 * @param ordered 
		 * @param inverseOrdered 
		 * @param unique 
		 */
		private SpaceTimeRuntimePropertyEnum(boolean onePrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower, boolean qualified, boolean inverseQualified, boolean ordered, boolean inverseOrdered, boolean unique) {
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
			this.qualified = qualified;
			this.inverseQualified = inverseQualified;
			this.ordered = ordered;
			this.inverseOrdered = inverseOrdered;
			this.unique = unique;
		}
	
		static public SpaceTimeRuntimePropertyEnum fromLabel(String label) {
			if ( space.getLabel().equals(label) ) {
				return space;
			}
			if ( name.getLabel().equals(label) ) {
				return name;
			}
			if ( time.getLabel().equals(label) ) {
				return time;
			}
			if ( universe.getLabel().equals(label) ) {
				return universe;
			}
			return null;
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
		
		public boolean isInverseOrdered() {
			return this.inverseOrdered;
		}
		
		public boolean isInverseQualified() {
			return this.inverseQualified;
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
		
		public boolean isOrdered() {
			return this.ordered;
		}
		
		public boolean isQualified() {
			return this.qualified;
		}
		
		public boolean isUnique() {
			return this.unique;
		}
		
		@Override
		public boolean isValid(int elementCount) {
			return (getUpper() == -1 || elementCount <= getUpper()) && elementCount >= getLower();
		}
	
	}
}