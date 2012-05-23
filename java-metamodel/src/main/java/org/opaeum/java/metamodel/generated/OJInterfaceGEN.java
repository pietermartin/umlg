/*
 * File generated by Grasland Grammar Generator on Dec 23, 2006 7:26:03 PM
 */
package org.opaeum.java.metamodel.generated;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.opaeum.java.metamodel.OJClassifier;
import org.opaeum.java.metamodel.OJElement;
import org.opaeum.java.metamodel.OJInterface;
import org.opaeum.java.metamodel.OJPackage;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.utilities.InvariantError;


/** Class ...
 */
abstract public class OJInterfaceGEN extends OJClassifier {
	private OJPackage f_myPackage = null;
	private Set<OJPathName> f_superInterfaces = new HashSet<OJPathName>();
	static protected boolean usesAllInstances = false;
	static protected List<OJInterface> allInstances = new ArrayList<OJInterface>();

	/** Default constructor for OJInterface
	 */
	protected OJInterfaceGEN() {
		super();
		if ( usesAllInstances ) {
			allInstances.add(((OJInterface)this));
		}
	}
	
	/** Constructor for OJInterfaceGEN
	 * 
	 * @param name 
	 * @param comment 
	 * @param isStatic 
	 * @param isFinal 
	 * @param isVolatile 
	 * @param uniqueNumber 
	 * @param isDerived 
	 * @param isAbstract 
	 */
	protected OJInterfaceGEN(String name, String comment, boolean isStatic, boolean isFinal, boolean isVolatile, int uniqueNumber, boolean isDerived, boolean isAbstract) {
		super();
		super.setName(name);
		super.setComment(comment);
		super.setStatic(isStatic);
		super.setFinal(isFinal);
		super.setVolatile(isVolatile);
		super.setUniqueNumber(uniqueNumber);
		super.setDerived(isDerived);
		super.setAbstract(isAbstract);
		if ( usesAllInstances ) {
			allInstances.add(((OJInterface)this));
		}
	}

	/** Implements the setter of association end myPackage
	 * 
	 * @param element 
	 */
	public void setMyPackage(OJPackage element) {
		if ( this.f_myPackage != element ) {
			if ( this.f_myPackage != null ) {
				this.f_myPackage.z_internalRemoveFromInterfaces( (OJInterface)((OJInterface)this) );
			}
			this.f_myPackage = element;
			if ( element != null ) {
				element.z_internalAddToInterfaces( (OJInterface)((OJInterface)this) );
			}
		}
	}
	
	/** Implements the getter for myPackage
	 */
	public OJPackage getMyPackage() {
		return f_myPackage;
	}
	
	/** Should NOT be used by clients! Implements the correct setting of the link for + myPackage : OJPackage 
						when a single element is added to it.
	 * 
	 * @param element 
	 */
	public void z_internalAddToMyPackage(OJPackage element) {
		this.f_myPackage = element;
	}
	
	/** Should NOT be used by clients! Implements the correct setting of the link for + myPackage : OJPackage 
						when a single element is removed to it.
	 * 
	 * @param element 
	 */
	public void z_internalRemoveFromMyPackage(OJPackage element) {
		this.f_myPackage = null;
	}
	
	/** Implements the getter for feature '+ superInterfaces : Set(OJPathName)'
	 */
	public Set<OJPathName> getSuperInterfaces() {
		return f_superInterfaces;
	}
	
	/** Implements the setter for feature '+ superInterfaces : Set(OJPathName)'
	 * 
	 * @param element 
	 */
	public void setSuperInterfaces(Set<OJPathName> element) {
		if ( f_superInterfaces != element ) {
			f_superInterfaces = element;
		}
	}
	
	/** Implements the add element function for feature '+ superInterfaces : Set(OJPathName)'
	 * 
	 * @param element 
	 */
	public void addToSuperInterfaces(OJPathName element) {
		if ( f_superInterfaces.contains(element) ) {
			return;
		}
		f_superInterfaces.add(element);
	}
	
	/** Implements the remove element function for feature '+ superInterfaces : Set(OJPathName)'
	 * 
	 * @param element 
	 */
	public void removeFromSuperInterfaces(OJPathName element) {
		f_superInterfaces.remove(element);
	}
	
	/** Implements the addition of a number of elements to feature '+ superInterfaces : Set(OJPathName)'
	 * 
	 * @param newElems 
	 */
	public void addToSuperInterfaces(Collection<OJPathName> newElems) {
		Iterator it = newElems.iterator();
		while ( (it.hasNext()) ) {
			Object item = it.next();
			if ( item instanceof OJPathName ) {
				addToSuperInterfaces((OJPathName)item);
			}
		}
	}
	
	/** Implements the removal of a number of elements from feature '+ superInterfaces : Set(OJPathName)'
	 * 
	 * @param oldElems 
	 */
	public void removeFromSuperInterfaces(Collection<OJPathName> oldElems) {
		Iterator it = oldElems.iterator();
		while ( (it.hasNext()) ) {
			Object item = it.next();
			if ( item instanceof OJPathName ) {
				removeFromSuperInterfaces((OJPathName)item);
			}
		}
	}
	
	/** Implements the removal of all elements from feature '+ superInterfaces : Set(OJPathName)'
	 */
	public void removeAllFromSuperInterfaces() {
		/* make a copy of the collection in order to avoid a ConcurrentModificationException*/
		Iterator it = new HashSet<OJPathName>(getSuperInterfaces()).iterator();
		while ( (it.hasNext()) ) {
			Object item = it.next();
			if ( item instanceof OJPathName ) {
				removeFromSuperInterfaces((OJPathName)item);
			}
		}
	}
	
	/** Checks all invariants of this object and returns a list of messages about broken invariants
	 */
	public List<InvariantError> checkAllInvariants() {
		List<InvariantError> result = new ArrayList<InvariantError>();
		return result;
	}
	
	/** Implements a check on the multiplicities of all attributes and association ends
	 */
	public List<InvariantError> checkMultiplicities() {
		List<InvariantError> result = new ArrayList<InvariantError>();
		if ( getMyPackage() == null ) {
			String message = "Mandatory feature 'myPackage' in object '";
			message = message + this.getIdString();
			message = message + "' of type '" + this.getClass().getName() + "' has no value.";
			result.add(new InvariantError(((OJInterface)this), message));
		}
		return result;
	}
	
	/** Default toString implementation for OJInterface
	 */
	public String toString() {
		String result = "";
		result = super.toString();
		return result;
	}
	
	/** Returns the default identifier for OJInterface
	 */
	public String getIdString() {
		String result = "";
		result = super.getIdString();
		return result;
	}
	
	/** Implements the OCL allInstances operation
	 */
	static public List allInstances() {
		if ( !usesAllInstances ) {
			throw new RuntimeException("allInstances is not implemented for ((OJInterface)this) class. Set usesAllInstances to true, if you want allInstances() implemented.");
		}
		return allInstances;
	}
	
	/** Returns a copy of this instance. True parts, i.e. associations marked
			'aggregate' or 'composite', and attributes, are copied as well. References to
			other objects, i.e. associations not marked 'aggregate' or 'composite', will not
			be copied. The returned copy will refer to the same objects as the original (this)
			instance.
	 */
	public OJElement getCopy() {
		OJInterface result = new OJInterface();
		this.copyInfoInto(result);
		return result;
	}
	
	/** Copies all attributes and associations of this instance into 'copy'.
			True parts, i.e. associations marked 'aggregate' or 'composite', and attributes, 
			are copied as well. References to other objects, i.e. associations not marked 
			'aggregate' or 'composite', will not be copied. The 'copy' will refer 
			to the same objects as the original (this) instance.
	 * 
	 * @param copy 
	 */
	public void copyInfoInto(OJInterface copy) {
		super.copyInfoInto(copy);
		if ( getMyPackage() != null ) {
			copy.setMyPackage(getMyPackage());
		}
		Iterator superInterfacesIt = new ArrayList<OJPathName>(getSuperInterfaces()).iterator();
		while ( superInterfacesIt.hasNext() ) {
			OJPathName elem = (OJPathName) superInterfacesIt.next();
			copy.addToSuperInterfaces(elem);
		}
	}

}