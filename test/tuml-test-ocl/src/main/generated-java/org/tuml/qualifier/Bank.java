package org.tuml.qualifier;

import com.tinkerpop.blueprints.CloseableIterable;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Index;
import com.tinkerpop.blueprints.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.collection.Multiplicity;
import org.tuml.runtime.collection.Qualifier;
import org.tuml.runtime.collection.TinkerOrderedSet;
import org.tuml.runtime.collection.TinkerQualifiedSet;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.collection.persistent.TinkerOrderedSetImpl;
import org.tuml.runtime.collection.persistent.TinkerQualifiedSetImpl;
import org.tuml.runtime.collection.persistent.TinkerSetImpl;
import org.tuml.runtime.domain.BaseTuml;
import org.tuml.runtime.domain.TumlNode;

public class Bank extends BaseTuml implements TumlNode {
	static final public long serialVersionUID = 1L;
	private TinkerSet<String> name;
	private TinkerQualifiedSet<Customer> customer;
	private TinkerOrderedSet<Employee> employee;

	/**
	 * constructor for Bank
	 * 
	 * @param vertex 
	 */
	public Bank(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/**
	 * default constructor for Bank
	 */
	public Bank() {
	}
	
	/**
	 * constructor for Bank
	 * 
	 * @param persistent 
	 */
	public Bank(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		defaultCreate();
		initialiseProperties();
		initVariables();
		createComponents();
		Edge edge = GraphDb.getDb().addEdge(null, GraphDb.getDb().getRoot(), this.vertex, "root");
		edge.setProperty("inClass", this.getClass().getName());
	}

	public void addToCustomer(Customer customer) {
		if ( customer != null ) {
			this.customer.add(customer);
		}
	}
	
	public void addToCustomer(TinkerSet<Customer> customer) {
		for ( Customer c : customer ) {
			this.addToCustomer(c);
		}
	}
	
	public void addToEmployee(Employee employee) {
		if ( employee != null ) {
			this.employee.add(employee);
		}
	}
	
	public void addToEmployee(TinkerOrderedSet<Employee> employee) {
		if ( !employee.isEmpty() ) {
			this.employee.addAll(employee);
		}
	}
	
	public void addToName(String name) {
		if ( name != null ) {
			this.name.add(name);
		}
	}
	
	public void clearCustomer() {
		this.customer.clear();
	}
	
	public void clearEmployee() {
		this.employee.clear();
	}
	
	public void clearName() {
		this.name.clear();
	}
	
	public void createComponents() {
	}
	
	@Override
	public void delete() {
		for ( Employee child : getEmployee() ) {
			child.delete();
		}
		for ( Customer child : getCustomer() ) {
			child.delete();
		}
		GraphDb.getDb().removeVertex(this.vertex);
	}
	
	public TinkerQualifiedSet<Customer> getCustomer() {
		return this.customer;
	}
	
	public Customer getCustomerForNameQualifierAccountNumberQualifier(String nameQualifier, Integer accountNumberQualifier) {
		Index<Edge> index = GraphDb.getDb().getIndex(getUid() + ":::" + BankRuntimePropertyEnum.customer.getLabel(), Edge.class);
		if ( index==null ) {
			return null;
		} else {
			String indexKey = "nameQualifieraccountNumberQualifier";
			String indexValue = nameQualifier==null?"___NULL___":nameQualifier;
			indexValue += accountNumberQualifier==null?"___NULL___":accountNumberQualifier;
			CloseableIterable<Edge> closeableIterable = index.get(indexKey, indexValue);
			Iterator<Edge> iterator = closeableIterable.iterator();
			if ( iterator.hasNext() ) {
				return new Customer(iterator.next().getVertex(Direction.IN));
			} else {
				return null;
			}
		}
	}
	
	/**
	 * Implements the ocl statement for derived property 'customerJohn001'
	 * <pre>
	 * package testoclmodel::org::tuml::qualifier
	 *     context Bank::customerJohn001 : Customer
	 *     derive: self.customer['john',1]
	 * endpackage
	 * </pre>
	 */
	public Customer getCustomerJohn001() {
		return getCustomerForNameQualifierAccountNumberQualifier("john", 1);
	}
	
	public TinkerOrderedSet<Employee> getEmployee() {
		return this.employee;
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
	
	public List<Qualifier> getQualifierForCustomer(Customer context) {
		List<Qualifier> result = new ArrayList<Qualifier>();
		result.add(new Qualifier(new String[]{"nameQualifier", "accountNumberQualifier"}, new String[]{context.getNameQualifier().toString() , context.getAccountNumberQualifier().toString() }, Multiplicity.ZERO_TO_ONE));
		return result;
	}
	
	/**
	 * getQualifiers is called from the collection in order to update the index used to implement the qualifier
	 * 
	 * @param tumlRuntimeProperty 
	 * @param node 
	 */
	@Override
	public List<Qualifier> getQualifiers(TumlRuntimeProperty tumlRuntimeProperty, TumlNode node) {
		List<Qualifier> result = Collections.emptyList();
		BankRuntimePropertyEnum runtimeProperty = BankRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result.isEmpty() ) {
			switch ( runtimeProperty ) {
				case customer:
					result = getQualifierForCustomer((Customer)node);
				break;
			
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
		BankRuntimePropertyEnum runtimeProperty = BankRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result == 0 ) {
			switch ( runtimeProperty ) {
				case name:
					result = name.size();
				break;
			
				case customer:
					result = customer.size();
				break;
			
				case employee:
					result = employee.size();
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
		this.employee =  new TinkerOrderedSetImpl<Employee>(this, BankRuntimePropertyEnum.employee);
		this.customer =  new TinkerQualifiedSetImpl<Customer>(this, BankRuntimePropertyEnum.customer);
		this.name =  new TinkerSetImpl<String>(this, BankRuntimePropertyEnum.name);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (BankRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case name:
				this.name =  new TinkerSetImpl<String>(this, BankRuntimePropertyEnum.name);
			break;
		
			case customer:
				this.customer =  new TinkerQualifiedSetImpl<Customer>(this, BankRuntimePropertyEnum.customer);
			break;
		
			case employee:
				this.employee =  new TinkerOrderedSetImpl<Employee>(this, BankRuntimePropertyEnum.employee);
			break;
		
		}
	}
	
	@Override
	public boolean isTinkerRoot() {
		return true;
	}
	
	public void removeFromCustomer(Customer customer) {
		if ( customer != null ) {
			this.customer.remove(customer);
		}
	}
	
	public void removeFromCustomer(TinkerSet<Customer> customer) {
		if ( !customer.isEmpty() ) {
			this.customer.removeAll(customer);
		}
	}
	
	public void removeFromEmployee(Employee employee) {
		if ( employee != null ) {
			this.employee.remove(employee);
		}
	}
	
	public void removeFromEmployee(TinkerOrderedSet<Employee> employee) {
		if ( !employee.isEmpty() ) {
			this.employee.removeAll(employee);
		}
	}
	
	public void removeFromName(String name) {
		if ( name != null ) {
			this.name.remove(name);
		}
	}
	
	public void removeFromName(TinkerSet<String> name) {
		if ( !name.isEmpty() ) {
			this.name.removeAll(name);
		}
	}
	
	public void setCustomer(TinkerSet<Customer> customer) {
		clearCustomer();
		addToCustomer(customer);
	}
	
	public void setEmployee(TinkerOrderedSet<Employee> employee) {
		clearEmployee();
		addToEmployee(employee);
	}
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}
	
	public void setName(String name) {
		clearName();
		addToName(name);
	}

	public enum BankRuntimePropertyEnum implements TumlRuntimeProperty {
		employee(false,true,true,"A_<bank>_<employee>",false,true,false,false,-1,0,false,false,true,false,true),
		customer(false,true,true,"A_<bank>_<customer>",false,true,false,false,1,0,true,false,false,false,true),
		name(true,true,false,"testoclmodel__org__tuml__qualifier__Bank__name",false,false,true,false,1,1,false,false,false,false,true);
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
		 * constructor for BankRuntimePropertyEnum
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
		private BankRuntimePropertyEnum(boolean onePrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower, boolean qualified, boolean inverseQualified, boolean ordered, boolean inverseOrdered, boolean unique) {
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
	
		static public BankRuntimePropertyEnum fromLabel(String label) {
			if ( employee.getLabel().equals(label) ) {
				return employee;
			}
			if ( customer.getLabel().equals(label) ) {
				return customer;
			}
			if ( name.getLabel().equals(label) ) {
				return name;
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