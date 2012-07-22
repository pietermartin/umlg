package org.tuml.qualifier;

import com.tinkerpop.blueprints.Vertex;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.collection.Qualifier;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.collection.persistent.TinkerSetImpl;
import org.tuml.runtime.domain.BaseTuml;
import org.tuml.runtime.domain.CompositionNode;
import org.tuml.runtime.domain.TumlNode;

public class Customer extends BaseTuml implements CompositionNode {
	static final public long serialVersionUID = 1L;
	private TinkerSet<String> name;
	private TinkerSet<Integer> accountNumber;
	private TinkerSet<Bank> bank;

	/**
	 * constructor for Customer
	 * 
	 * @param compositeOwner 
	 */
	public Customer(Bank compositeOwner) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		initialiseProperties();
		initVariables();
		createComponents();
		addToBank(compositeOwner);
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
	}
	
	/**
	 * constructor for Customer
	 * 
	 * @param vertex 
	 */
	public Customer(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/**
	 * default constructor for Customer
	 */
	public Customer() {
	}
	
	/**
	 * constructor for Customer
	 * 
	 * @param persistent 
	 */
	public Customer(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
		initialiseProperties();
		initVariables();
		createComponents();
	}

	public void addToAccountNumber(Integer accountNumber) {
		if ( accountNumber != null ) {
			this.accountNumber.add(accountNumber);
		}
	}
	
	public void addToBank(Bank bank) {
		if ( bank != null ) {
			this.bank.add(bank);
		}
	}
	
	public void addToName(String name) {
		if ( name != null ) {
			this.name.add(name);
		}
	}
	
	public void clearAccountNumber() {
		this.accountNumber.clear();
	}
	
	public void clearBank() {
		this.bank.clear();
	}
	
	public void clearName() {
		this.name.clear();
	}
	
	public void createComponents() {
	}
	
	@Override
	public void delete() {
		GraphDb.getDb().removeVertex(this.vertex);
	}
	
	public Integer getAccountNumber() {
		TinkerSet<Integer> tmp = this.accountNumber;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
	}
	
	/**
	 * Implements the ocl statement for derived property 'accountNumberQualifier'
	 * <pre>
	 * package testoclmodel::org::tuml::qualifier
	 *     context Customer::accountNumberQualifier : Integer
	 *     derive: self.accountNumber
	 * endpackage
	 * </pre>
	 */
	public Integer getAccountNumberQualifier() {
		return getAccountNumber();
	}
	
	public Bank getBank() {
		TinkerSet<Bank> tmp = this.bank;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
	}
	
	/**
	 * Implements the ocl statement for derived property 'employeeQualifier'
	 * <pre>
	 * package testoclmodel::org::tuml::qualifier
	 *     context Customer::employeeQualifier : Employee
	 *     derive: self.bank.employee->asSequence()->first()
	 * endpackage
	 * </pre>
	 */
	public Employee getEmployeeQualifier() {
		return getBank().getEmployee().asSequence().first();
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
	
	/**
	 * Implements the ocl statement for derived property 'nameQualifier'
	 * <pre>
	 * package testoclmodel::org::tuml::qualifier
	 *     context Customer::nameQualifier : String
	 *     derive: self.name
	 * endpackage
	 * </pre>
	 */
	public String getNameQualifier() {
		return getName();
	}
	
	@Override
	public int getObjectVersion() {
		return TinkerIdUtilFactory.getIdUtil().getVersion(this.vertex);
	}
	
	@Override
	public TumlNode getOwningObject() {
		return getBank();
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
		CustomerRuntimePropertyEnum runtimeProperty = CustomerRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
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
		CustomerRuntimePropertyEnum runtimeProperty = CustomerRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result == 0 ) {
			switch ( runtimeProperty ) {
				case bank:
					result = bank.size();
				break;
			
				case name:
					result = name.size();
				break;
			
				case accountNumber:
					result = accountNumber.size();
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
		this.accountNumber =  new TinkerSetImpl<Integer>(this, CustomerRuntimePropertyEnum.accountNumber);
		this.name =  new TinkerSetImpl<String>(this, CustomerRuntimePropertyEnum.name);
		this.bank =  new TinkerSetImpl<Bank>(this, CustomerRuntimePropertyEnum.bank);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (CustomerRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case bank:
				this.bank =  new TinkerSetImpl<Bank>(this, CustomerRuntimePropertyEnum.bank);
			break;
		
			case name:
				this.name =  new TinkerSetImpl<String>(this, CustomerRuntimePropertyEnum.name);
			break;
		
			case accountNumber:
				this.accountNumber =  new TinkerSetImpl<Integer>(this, CustomerRuntimePropertyEnum.accountNumber);
			break;
		
		}
	}
	
	@Override
	public boolean isTinkerRoot() {
		return false;
	}
	
	public void removeFromAccountNumber(Integer accountNumber) {
		if ( accountNumber != null ) {
			this.accountNumber.remove(accountNumber);
		}
	}
	
	public void removeFromAccountNumber(TinkerSet<Integer> accountNumber) {
		if ( !accountNumber.isEmpty() ) {
			this.accountNumber.removeAll(accountNumber);
		}
	}
	
	public void removeFromBank(Bank bank) {
		if ( bank != null ) {
			this.bank.remove(bank);
		}
	}
	
	public void removeFromBank(TinkerSet<Bank> bank) {
		if ( !bank.isEmpty() ) {
			this.bank.removeAll(bank);
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
	
	public void setAccountNumber(Integer accountNumber) {
		clearAccountNumber();
		addToAccountNumber(accountNumber);
	}
	
	public void setBank(Bank bank) {
		clearBank();
		addToBank(bank);
	}
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}
	
	public void setName(String name) {
		clearName();
		addToName(name);
	}

	public enum CustomerRuntimePropertyEnum implements TumlRuntimeProperty {
		accountNumber(true,true,false,"testoclmodel__org__tuml__qualifier__Customer__accountNumber",false,false,true,false,1,1,false,false,false,false,true),
		name(true,true,false,"testoclmodel__org__tuml__qualifier__Customer__name",false,false,true,false,1,1,false,false,false,false,true),
		bank(false,false,false,"A_<bank>_<customer>",false,false,true,false,1,1,false,true,false,false,true);
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
		 * constructor for CustomerRuntimePropertyEnum
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
		private CustomerRuntimePropertyEnum(boolean onePrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower, boolean qualified, boolean inverseQualified, boolean ordered, boolean inverseOrdered, boolean unique) {
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
	
		static public CustomerRuntimePropertyEnum fromLabel(String label) {
			if ( accountNumber.getLabel().equals(label) ) {
				return accountNumber;
			}
			if ( name.getLabel().equals(label) ) {
				return name;
			}
			if ( bank.getLabel().equals(label) ) {
				return bank;
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