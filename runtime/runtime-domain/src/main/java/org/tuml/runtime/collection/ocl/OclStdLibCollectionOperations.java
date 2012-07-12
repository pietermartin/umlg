package org.tuml.runtime.collection.ocl;

/**
 * From ocl spec v2.3.1
 */
public interface OclStdLibCollectionOperations {

	/**
	 * = (c : Collection(T)) : Boolean
	 * 
	 * True if c is a collection of the same kind as self and contains the same
	 * elements in the same quantities and in the same order, in the case of an
	 * ordered collection type.
	 */
	boolean equals(OclStdLibCollectionOperations c);

	/**
	 * <> (c : Collection(T)) : Boolean
	 * 
	 * True if c is not equal to self. post: result = not (self = c)
	 */
//	boolean notEquals();

	/**
	 * size() : Integer
	 * 
	 * The number of elements in the collection self. post: result =
	 * self->iterate(elem; acc : Integer = 0 | acc + 1)
	 * 
	 * @return int
	 */
	int size();

	/**
	 * includes(object : T) : Boolean
	 * 
	 * True if object is an element of self, false otherwise. post: result =
	 * (self->count(object) > 0)
	 */
//	boolean includes(Object t);

	/**
	 * excludes(object : T) : Boolean
	 * 
	 * True if object is not an element of self, false otherwise. post: result =
	 * (self->count(object) = 0)
	 */
//	boolean excludes(Object t);
}
