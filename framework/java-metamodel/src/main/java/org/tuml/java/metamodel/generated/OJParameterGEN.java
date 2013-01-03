/*
 * File generated by Grasland Grammar Generator on Dec 23, 2006 7:26:03 PM
 */
package org.tuml.java.metamodel.generated;

import java.util.ArrayList;
import java.util.List;

import org.tuml.java.metamodel.OJElement;
import org.tuml.java.metamodel.OJParameter;
import org.tuml.java.metamodel.OJPathName;
import org.tuml.java.metamodel.utilities.InvariantError;


/** Class ...
 */
abstract public class OJParameterGEN extends OJElement {
	private OJPathName f_type = null;
	static protected boolean usesAllInstances = false;
	static protected List<OJParameter> allInstances = new ArrayList<OJParameter>();

	/** Constructor for OJParameterGEN
	 * 
	 * @param name 
	 * @param comment 
	 */
	protected OJParameterGEN(String name, String comment) {
		super();
		super.setName(name);
		super.setComment(comment);
		if ( usesAllInstances ) {
			allInstances.add(((OJParameter)this));
		}
	}
	
	/** Default constructor for OJParameter
	 */
	protected OJParameterGEN() {
		super();
		if ( usesAllInstances ) {
			allInstances.add(((OJParameter)this));
		}
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
		if ( getType() == null ) {
			String message = "Mandatory feature 'type' in object '";
			message = message + this.getIdString();
			message = message + "' of type '" + this.getClass().getName() + "' has no value.";
			result.add(new InvariantError(((OJParameter)this), message));
		}
		return result;
	}
	
	/** Default toString implementation for OJParameter
	 */
	public String toString() {
		String result = "";
		result = super.toString();
		return result;
	}
	
	/** Returns the default identifier for OJParameter
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
			throw new RuntimeException("allInstances is not implemented for ((OJParameter)this) class. Set usesAllInstances to true, if you want allInstances() implemented.");
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
		OJParameter result = new OJParameter();
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
	public void copyInfoInto(OJParameter copy) {
		super.copyInfoInto(copy);
		if ( getType() != null ) {
			copy.setType(getType());
		}
	}

}