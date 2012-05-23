/*
 * File generated by Grasland Grammar Generator on Dec 23, 2006 7:26:03 PM
 */
package org.opaeum.java.metamodel.generated;

import java.util.ArrayList;
import java.util.List;

import org.opaeum.java.metamodel.OJClass;
import org.opaeum.java.metamodel.OJElement;
import org.opaeum.java.metamodel.OJField;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.OJVisibleElement;
import org.opaeum.java.metamodel.utilities.InvariantError;


/** Class ...
 */
abstract public class OJFieldGEN extends OJVisibleElement {
	private String f_initExp = "";
	private OJClass f_owner = null;
	private OJPathName f_type = null;
	static protected boolean usesAllInstances = false;
	static protected List<OJField> allInstances = new ArrayList<OJField>();

	/** Constructor for OJFieldGEN
	 * 
	 * @param name 
	 * @param comment 
	 * @param isStatic 
	 * @param isFinal 
	 * @param isVolatile 
	 * @param initExp 
	 */
	protected OJFieldGEN(String name, String comment, boolean isStatic, boolean isFinal, boolean isVolatile, String initExp) {
		super();
		super.setName(name);
		super.setComment(comment);
		super.setStatic(isStatic);
		super.setFinal(isFinal);
		super.setVolatile(isVolatile);
		this.setInitExp(initExp);
		this.setInitExp( null );
		if ( usesAllInstances ) {
			allInstances.add(((OJField)this));
		}
	}
	
	/** Default constructor for OJField
	 */
	protected OJFieldGEN() {
		super();
		this.setInitExp( null );
		if ( usesAllInstances ) {
			allInstances.add(((OJField)this));
		}
	}

	/** Implements the getter for feature '+ initExp : String'
	 */
	public String getInitExp() {
		return f_initExp;
	}
	
	/** Implements the setter for feature '+ initExp : String'
	 * 
	 * @param element 
	 */
	public void setInitExp(String element) {
		if ( f_initExp != element ) {
			f_initExp = element;
		}
	}
	
	/** Implements the setter of association end owner
	 * 
	 * @param element 
	 */
	public void setOwner(OJClass element) {
		if ( this.f_owner != element ) {
			if ( this.f_owner != null ) {
				this.f_owner.z_internalRemoveFromFields( (OJField)((OJField)this) );
			}
			this.f_owner = element;
			if ( element != null ) {
				element.z_internalAddToFields( (OJField)((OJField)this) );
			}
		}
	}
	
	/** Implements the getter for owner
	 */
	public OJClass getOwner() {
		return f_owner;
	}
	
	/** Should NOT be used by clients! Implements the correct setting of the link for + owner : OJClass 
						when a single element is added to it.
	 * 
	 * @param element 
	 */
	public void z_internalAddToOwner(OJClass element) {
		this.f_owner = element;
	}
	
	/** Should NOT be used by clients! Implements the correct setting of the link for + owner : OJClass 
						when a single element is removed to it.
	 * 
	 * @param element 
	 */
	public void z_internalRemoveFromOwner(OJClass element) {
		this.f_owner = null;
	}
	
	/** Implements the getter for feature '+ type : OJPathName'
	 */
	public OJPathName getType() {
		return f_type;
	}
	
	/** Implements the setter for feature '+ type : OJPathName'
	 * 
	 * @param element 
	 */
	public void setType(OJPathName element) {
		if ( f_type != element ) {
			f_type = element;
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
		if ( getOwner() == null ) {
			String message = "Mandatory feature 'owner' in object '";
			message = message + this.getIdString();
			message = message + "' of type '" + this.getClass().getName() + "' has no value.";
			result.add(new InvariantError(((OJField)this), message));
		}
		if ( getType() == null ) {
			String message = "Mandatory feature 'type' in object '";
			message = message + this.getIdString();
			message = message + "' of type '" + this.getClass().getName() + "' has no value.";
			result.add(new InvariantError(((OJField)this), message));
		}
		return result;
	}
	
	/** Default toString implementation for OJField
	 */
	public String toString() {
		String result = "";
		result = super.toString();
		if ( this.getInitExp() != null ) {
			result = result + " initExp:" + this.getInitExp();
		}
		return result;
	}
	
	/** Returns the default identifier for OJField
	 */
	public String getIdString() {
		String result = "";
		if ( this.getInitExp() != null ) {
			result = result + this.getInitExp();
		}
		return result;
	}
	
	/** Implements the OCL allInstances operation
	 */
	static public List allInstances() {
		if ( !usesAllInstances ) {
			throw new RuntimeException("allInstances is not implemented for ((OJField)this) class. Set usesAllInstances to true, if you want allInstances() implemented.");
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
		OJField result = new OJField();
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
	public void copyInfoInto(OJField copy) {
		super.copyInfoInto(copy);
		copy.setInitExp(getInitExp());
		if ( getOwner() != null ) {
			copy.setOwner(getOwner());
		}
		if ( getType() != null ) {
			copy.setType(getType());
		}
	}

}