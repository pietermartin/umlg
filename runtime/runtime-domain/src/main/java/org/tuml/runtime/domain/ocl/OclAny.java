package org.tuml.runtime.domain.ocl;

import org.tuml.runtime.domain.TumlNode;

public interface OclAny {
	/**
	 * =(object2 : OclAny) : Boolean
	 * True if self is the same object as object2. Infix operator.
     * post: result = (self = object2)
     * 
	 * @param oclAny
	 * @return
	 */
	Boolean equals(OclAny oclAny);
	/**
	 * <> (object2 : OclAny) : Boolean
     * True if self is a different object from object2. Infix operator.
     * post: result = not (self = object2)
	 * 
	 * @param oclAny
	 * @return
	 */
	Boolean notEquals(OclAny oclAny);
	/**
	 * oclIsNew() : Boolean
	 * 
     * Can only be used in a postcondition. Evaluates to true if the self is created during performing the operation (for instance,
     * it didn’t exist at precondition time).
     * post: self@pre.oclIsUndefined()
     * 
	 * @return
	 */
	Boolean oclIsNew();
	/**
	 * oclIsUndefined() : Boolean
	 * 
     * Evaluates to true if the self is equal to invalid or equal to null.
     * post: result = self.isTypeOf( OclVoid ) or self.isTypeOf(OclInvalid)
     * 
	 * @return
	 */
	Boolean oclIsUndefined();
	/**
	 * oclIsInvalid() : Boolean
	 * 
     * Evaluates to true if the self is equal to OclInvalid.
     * post: result = self.isTypeOf( OclInvalid)
	 * @return
	 */
	Boolean oclIsInvalid();
	/**
	 * oclAsType(type : Classifier) : T
	 * 
	 * Evaluates to self, where self is of the type identified by T. The type T may be any classifier defined in the UML model;
	 * if the actual type of self at evaluation time does not conform to T, then the oclAsType operation evaluates to invalid.
	 * In the case of feature redefinition, casting an object to a supertype of its actual type does not access the supertype's
	 * definition of the feature; according to the semantics of redefinition, the redefined feature simply does not exist for the
	 * object. However, when casting to a supertype, any features additionally defined by the subtype are suppressed.
	 * post: (result = self) and result.oclIsTypeOf( t )
	 * 
	 * @param classifier
	 * @return
	 */
	<T> T oclAsType(T classifier);
	/**
	 * oclIsTypeOf(type : Classifier) : Boolean
	 * 
     * Evaluates to true if self is of the type t but not a subtype of t
     * post: self.oclType() = type
	 * @param classifier
	 * @return
	 */
	Boolean oclIsTypeOf(TumlNode classifier);
	/**
	 * oclIsKindOf(type : Classifier) : Boolean
	 * 
	 * Evaluates to true if the type of self conforms to t. That is, self is of type t or a subtype of t.
	 * post: self.oclType().conformsTo(type)
	 * 
	 * @param classifier
	 * @return
	 */
	Boolean oclIsKindOf(TumlNode classifier);
	/**
	 * oclIsInState(statespec : OclState) : Boolean
	 * 
	 * Evaluates to true if the self is in the state indentified by statespec.
	 * post: -- TBD
	 * 
	 * @param state
	 * @return
	 */
	Boolean oclIsInState(OclState state);
	/**
	 * oclType() : Classifier
	 * 
	 * Evaluates to the type of which self is an instance.
	 * post: self.oclIsTypeOf(result)
	 * 
	 * @return
	 */
	TumlNode oclType();
	/**
	 * oclLocale : String
	 * 
	 * Defines the default locale for local-dependent library operations such as String::toUpperCase().
	 * 
	 * @return
	 */
	String oclLocale();
}
