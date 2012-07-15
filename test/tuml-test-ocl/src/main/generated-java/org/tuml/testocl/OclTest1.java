package org.tuml.testocl;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

import java.util.*;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.collection.Qualifier;
import org.tuml.runtime.collection.TinkerBag;
import org.tuml.runtime.collection.TinkerSequence;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.collection.impl.TinkerBagImpl;
import org.tuml.runtime.collection.impl.TinkerSetImpl;
import org.tuml.runtime.collection.ocl.*;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.TinkerNode;

public class OclTest1 extends BaseTinker implements TinkerNode {
	static final public long serialVersionUID = 1L;
	private TinkerSet<String> property1;
	private TinkerBag<OclTestCollection> oclTestCollection;

	/**
	 * constructor for OclTest1
	 * 
	 * @param vertex 
	 */
	public OclTest1(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/**
	 * default constructor for OclTest1
	 */
	public OclTest1() {
	}
	
	/**
	 * constructor for OclTest1
	 * 
	 * @param persistent 
	 */
	public OclTest1(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		defaultCreate();
		initialiseProperties();
		initVariables();
		createComponents();
		Edge edge = GraphDb.getDb().addEdge(null, GraphDb.getDb().getRoot(), this.vertex, "root");
		edge.setProperty("inClass", this.getClass().getName());
	}

	public void addToOclTestCollection(OclTestCollection oclTestCollection) {
		if ( oclTestCollection != null ) {
			this.oclTestCollection.add(oclTestCollection);
		}
	}
	
	public void addToOclTestCollection(TinkerBag<OclTestCollection> oclTestCollection) {
		if ( !oclTestCollection.isEmpty() ) {
			this.oclTestCollection.addAll(oclTestCollection);
		}
	}
	
	public void addToProperty1(String property1) {
		if ( property1 != null ) {
			this.property1.add(property1);
		}
	}
	
	public void clearOclTestCollection() {
		this.oclTestCollection.clear();
	}
	
	public void clearProperty1() {
		this.property1.clear();
	}
	
	public void createComponents() {
	}
	
	@Override
	public void delete() {
		for ( OclTestCollection child : getOclTestCollection() ) {
			child.delete();
		}
		GraphDb.getDb().removeVertex(this.vertex);
	}
	
	/**
	 * Implements the ocl statement for derived property 'derivedProperty1'
	 * <pre>
	 * package testoclmodel::org::tuml::testocl
	 *     context OclTest1::derivedProperty1 : String
	 *     derive: self.property1
	 * endpackage
	 * </pre>
	 */
	public String getDerivedProperty1() {
		return getProperty1();
	}
	
	@Override
	public Long getId() {
		return TinkerIdUtilFactory.getIdUtil().getId(this.vertex);
	}
	
	@Override
	public int getObjectVersion() {
		return TinkerIdUtilFactory.getIdUtil().getVersion(this.vertex);
	}
	
	/**
	 * Implements the ocl statement for derived property 'oclAny'
	 * <pre>
	 * package testoclmodel::org::tuml::testocl
	 *     context OclTest1::oclAny : OclTestCollection
	 *     derive: self.oclTestCollection->any(name <> 'john')
	 * endpackage
	 * </pre>
	 */
	public OclTestCollection getOclAny() {
		return getOclTestCollection().any(new BooleanExpressionEvaluator<OclTestCollection>() {
		    @Override
		    public Boolean evaluate(OclTestCollection temp1) {
		        return temp1.getName().equals("john") == false;
		    }
		});
	}
	
	/**
	 * Implements the ocl statement for derived property 'oclCollectAsSet'
	 * <pre>
	 * package testoclmodel::org::tuml::testocl
	 *     context OclTest1::oclCollectAsSet : Set(OclTestCollection2)
	 *     derive: self.oclTestCollection.oclTestCollection2->asSet()
	 * endpackage
	 * </pre>
	 */
	public TinkerSet<OclTestCollection2> getOclCollectAsSet() {
		return getOclTestCollection().<OclTestCollection2, Set<OclTestCollection2>>collect(new BodyExpressionEvaluator<Set<OclTestCollection2>, OclTestCollection>() {
		    @Override
		    public Set<OclTestCollection2> evaluate(OclTestCollection temp1) {
		        return temp1.getOclTestCollection2();
		    }
		}).asSet();
	}
	
	/**
	 * Implements the ocl statement for derived property 'oclCollectName'
	 * <pre>
	 * package testoclmodel::org::tuml::testocl
	 *     context OclTest1::oclCollectName : Bag(String)
	 *     derive: self.oclTestCollection.oclTestCollection2.name
	 * endpackage
	 * </pre>
	 */
	public TinkerBag<String> getOclCollectName() {
		return getOclTestCollection().<OclTestCollection2, Set<OclTestCollection2>>collect(new BodyExpressionEvaluator<Set<OclTestCollection2>, OclTestCollection>() {
		    @Override
		    public Set<OclTestCollection2> evaluate(OclTestCollection temp1) {
		        return temp1.getOclTestCollection2();
		    }
		}).<String, String>collect(new BodyExpressionEvaluator<String, OclTestCollection2>() {
		    @Override
		    public String evaluate(OclTestCollection2 temp2) {
		        return temp2.getName();
		    }
		});
	}
	
	/**
	 * Implements the ocl statement for derived property 'oclCollectNameAsSet'
	 * <pre>
	 * package testoclmodel::org::tuml::testocl
	 *     context OclTest1::oclCollectNameAsSet : Set(String)
	 *     derive: self.oclTestCollection.name->asSet()
	 * endpackage
	 * </pre>
	 */
	public TinkerSet<String> getOclCollectNameAsSet() {
		return getOclTestCollection().<String, String>collect(new BodyExpressionEvaluator<String, OclTestCollection>() {
		    @Override
		    public String evaluate(OclTestCollection temp1) {
		        return temp1.getName();
		    }
		}).asSet();
	}
	
	/**
	 * Implements the ocl statement for derived property 'oclCollectNested'
	 * <pre>
	 * package testoclmodel::org::tuml::testocl
	 *     context OclTest1::oclCollectNested : Bag(OclTestCollection2)
	 *     derive: self.oclTestCollection->collectNested(oclTestCollection2)->flatten()
	 * endpackage
	 * </pre>
	 */
	public TinkerBag<OclTestCollection2> getOclCollectNested() {
		return getOclTestCollection().collectNested(new BodyExpressionEvaluator<Set<OclTestCollection2>, OclTestCollection>() {
		    @Override
		    public Set<OclTestCollection2> evaluate(OclTestCollection temp1) {
		        return temp1.getOclTestCollection2();
		    }
		}).flatten();
	}
	
	/**
	 * Implements the ocl statement for derived property 'oclIterateExp'
	 * <pre>
	 * package testoclmodel::org::tuml::testocl
	 *     context OclTest1::oclIterateExp : String
	 *     derive: self.oclTestCollection->iterate(iter : OclTestCollection; acc : String = '' | acc.concat(iter.name) )
	 * endpackage
	 * </pre>
	 */
	public String getOclIterateExp() {
		return getOclTestCollection().iterate(new IterateExpressionAccumulator<String, OclTestCollection>() {
		    @Override
		    public String accumulate(String acc, OclTestCollection iter) {
		        return acc.concat(iter.getName());
		    }
		
		    @Override
		    public String initAccumulator() {
		        String acc = "";
		        return acc;
		    }
		});
	}
	
	/**
	 * Implements the ocl statement for derived property 'oclSelect'
	 * <pre>
	 * package testoclmodel::org::tuml::testocl
	 *     context OclTest1::oclSelect : Bag(OclTestCollection)
	 *     derive: self.oclTestCollection->select(name='john')
	 * endpackage
	 * </pre>
	 */
	public TinkerBag<OclTestCollection> getOclSelect() {
		return getOclTestCollection().select(new BooleanExpressionEvaluator<OclTestCollection>() {
		    @Override
		    public Boolean evaluate(OclTestCollection temp1) {
		        return temp1.getName().equals("john");
		    }
		});
	}
	
	/**
	 * Implements the ocl statement for derived property 'oclSelectCollectAsSequence'
	 * <pre>
	 * package testoclmodel::org::tuml::testocl
	 *     context OclTest1::oclSelectCollectAsSequence : Sequence(String)
	 *     derive: self.oclTestCollection->select(name='john')->collect(oclTestCollection2)->collect(name)->asSequence()
	 * endpackage
	 * </pre>
	 */
	public TinkerSequence<String> getOclSelectCollectAsSequence() {
		return getOclTestCollection().select(new BooleanExpressionEvaluator<OclTestCollection>() {
		    @Override
		    public Boolean evaluate(OclTestCollection temp1) {
		        return temp1.getName().equals("john");
		    }
		}).<OclTestCollection2, Set<OclTestCollection2>>collect(new BodyExpressionEvaluator<Set<OclTestCollection2>, OclTestCollection>() {
		    @Override
		    public Set<OclTestCollection2> evaluate(OclTestCollection temp2) {
		        return temp2.getOclTestCollection2();
		    }
		}).<String, String>collect(new BodyExpressionEvaluator<String, OclTestCollection2>() {
		    @Override
		    public String evaluate(OclTestCollection2 temp3) {
		        return temp3.getName();
		    }
		}).asSequence();
	}
	
	public TinkerBag<OclTestCollection> getOclTestCollection() {
		return this.oclTestCollection;
	}
	
	public String getProperty1() {
		TinkerSet<String> tmp = this.property1;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
	}
	
	/**
	 * getQualifiers is called from the collection in order to update the index used to implement the qualifier
	 * 
	 * @param tumlRuntimeProperty 
	 * @param node 
	 */
	@Override
	public List<Qualifier> getQualifiers(TumlRuntimeProperty tumlRuntimeProperty, TinkerNode node) {
		List<Qualifier> result = Collections.emptyList();
		OclTest1RuntimePropertyEnum runtimeProperty = OclTest1RuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result.isEmpty() ) {
			switch ( runtimeProperty ) {
				default:
					result = Collections.emptyList();
				break;
			}
		
		}
		return result;
	}
	
	/**
	 * getSize is called from the collection in order to update the index used to implement a sequance's index
	 * 
	 * @param tumlRuntimeProperty 
	 */
	@Override
	public int getSize(TumlRuntimeProperty tumlRuntimeProperty) {
		int result = 0;
		OclTest1RuntimePropertyEnum runtimeProperty = OclTest1RuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result == 0 ) {
			switch ( runtimeProperty ) {
				case property1:
					result = property1.size();
				break;
			
				case oclTestCollection:
					result = oclTestCollection.size();
				break;
			
				default:
					result = 0;
				break;
			}
		
		}
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
		this.oclTestCollection =  new TinkerBagImpl<OclTestCollection>(this, OclTest1RuntimePropertyEnum.oclTestCollection);
		this.property1 =  new TinkerSetImpl<String>(this, OclTest1RuntimePropertyEnum.property1);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (OclTest1RuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case property1:
				this.property1 =  new TinkerSetImpl<String>(this, OclTest1RuntimePropertyEnum.property1);
			break;
		
			case oclTestCollection:
				this.oclTestCollection =  new TinkerBagImpl<OclTestCollection>(this, OclTest1RuntimePropertyEnum.oclTestCollection);
			break;
		
		}
	}
	
	@Override
	public boolean isTinkerRoot() {
		return true;
	}
	
	public void removeFromOclTestCollection(OclTestCollection oclTestCollection) {
		if ( oclTestCollection != null ) {
			this.oclTestCollection.remove(oclTestCollection);
		}
	}
	
	public void removeFromOclTestCollection(TinkerBag<OclTestCollection> oclTestCollection) {
		if ( !oclTestCollection.isEmpty() ) {
			this.oclTestCollection.removeAll(oclTestCollection);
		}
	}
	
	public void removeFromProperty1(String property1) {
		if ( property1 != null ) {
			this.property1.remove(property1);
		}
	}
	
	public void removeFromProperty1(TinkerSet<String> property1) {
		if ( !property1.isEmpty() ) {
			this.property1.removeAll(property1);
		}
	}
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}
	
	public void setOclTestCollection(TinkerBag<OclTestCollection> oclTestCollection) {
		clearOclTestCollection();
		addToOclTestCollection(oclTestCollection);
	}
	
	public void setProperty1(String property1) {
		clearProperty1();
		addToProperty1(property1);
	}

	public enum OclTest1RuntimePropertyEnum implements TumlRuntimeProperty {
		oclTestCollection(false,true,true,"A_<oclTest1>_<oclTestCollection>",false,true,false,false,-1,1,false,false,false,false,false),
		property1(true,true,false,"testoclmodel__org__tuml__testocl__OclTest1__property1",false,false,true,false,1,1,false,false,false,false,true);
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
		/**
		 * constructor for OclTest1RuntimePropertyEnum
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
		private OclTest1RuntimePropertyEnum(boolean onePrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower, boolean qualified, boolean inverseQualified, boolean ordered, boolean inverseOrdered, boolean unique) {
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
	
		static public OclTest1RuntimePropertyEnum fromLabel(String label) {
			if ( oclTestCollection.getLabel().equals(label) ) {
				return oclTestCollection;
			}
			if ( property1.getLabel().equals(label) ) {
				return property1;
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