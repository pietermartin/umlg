package org.tuml.runtime.collection.ocl;

import org.tuml.runtime.collection.TinkerBag;
import org.tuml.runtime.collection.TinkerCollection;
import org.tuml.runtime.collection.TinkerOrderedSet;
import org.tuml.runtime.collection.TinkerSequence;
import org.tuml.runtime.collection.TinkerSet;


/**
 * From ocl spec v2.3.1
 */
public interface OclStdLibCollection<E> {

	/**
	 * = (c : Collection(T)) : Boolean
	 * 
	 * True if c is a collection of the same kind as self and contains the same
	 * elements in the same quantities and in the same order, in the case of an
	 * ordered collection type.
	 */
	boolean equals(OclStdLibCollection<E> c);

	/**
	 * <> (c : Collection(T)) : Boolean
	 * 
	 * True if c is not equal to self. post: result = not (self = c)
	 */
	// boolean notEquals();

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
	// boolean includes(Object t);

	/**
	 * excludes(object : T) : Boolean
	 * 
	 * True if object is not an element of self, false otherwise. post: result =
	 * (self->count(object) = 0)
	 */
	// boolean excludes(Object t);


	<R> R iterate(IterateExpressionAccumulator<R, E> v);
	
	/**
	 * The subset of set for which expr is true. source->select(iterator | body)
	 * = source->iterate(iterator; result : Set(T) = Set{} | if body then
	 * result->including(iterator) else result endif) select may have at most
	 * one iterator variable.
	 */
	TinkerCollection<E> select(BooleanExpressionEvaluator<E> e);

	/**
	 * Returns any element in the source collection for which body evaluates to
	 * true. If there is more than one element for which body is true, one of
	 * them is returned. There must be at least one element fulfilling body,
	 * otherwise the result of this IteratorExp is null.
	 * 
	 * source->any(iterator | body) = source->select(iterator |
	 * body)->asSequence()->first()
	 * 
	 * any may have at most one iterator variable.
	 * 
	 * @param e
	 * @return
	 */
	E any(BooleanExpressionEvaluator<E> e);

	/**
	 * The Collection of elements that results from applying body to every
	 * member of the source set. The result is flattened. Notice that this is
	 * based on collectNested, which can be of different type depending on the
	 * type of source. collectNested is defined individually for each subclass
	 * of CollectionType.
	 * 
	 * source->collect (iterator | body) = source->collectNested (iterator |
	 * body)->flatten()
	 * 
	 * collect may have at most one iterator variable.
	 * 
	 * @param e
	 * @return
	 */
	<T, R> TinkerCollection<T> collect(BodyExpressionEvaluator<R, E> e);
	
	/**
     * The Bag of elements which results from applying body to every member of the source set.
     * 		source->collectNested(iterator | body) =
     * 			source->iterate(iterator; result : Bag(body.type) = Bag{} |
     * 				result->including(body ) )
     * 
     * collectNested may have at most one iterator variable.
	 * 
	 * @param e
	 * @return
	 */
	<R> TinkerCollection<R> collectNested(BodyExpressionEvaluator<R, E> e);	

	/**
	 * flatten() : Collection(T2)
     * If the element type is not a collection type, this results in the same collection as self. If the element type is a collection
     * type, the result is a collection containing all the elements of all the recursively flattened elements of self.
     * [1] Well-formedness rules
     * [2] [1] A collection cannot contain invalid values.
     * 		context Collection
     * 		inv: self->forAll(not oclIsInvalid())
     * 
	 * @return
	 */
	<R> TinkerCollection<R> flatten();	

	/**
	 * The Set containing all the elements from self, with duplicates removed.
     * 		post: result->forAll(elem | self ->includes(elem))
     * 		post: self ->forAll(elem | result->includes(elem))
	 */
	TinkerSet<E> asSet();
	
	/**
	 * An OrderedSet that contains all the elements from self, with duplicates removed, in an order dependent on the particular
     * concrete collection type.
     * 		post: result->forAll(elem | self->includes(elem))
     * 		post: self ->forAll(elem | result->includes(elem))
	 */
	TinkerOrderedSet<E> asOrderedSet();
	
	/**
	 * A Sequence that contains all the elements from self, in an order dependent on the particular concrete collection type.
     * 		post: result->forAll(elem | self->includes(elem))
     * 		post: self ->forAll(elem | result->includes(elem))
	 */
	TinkerSequence<E> asSequence();
	
	/**
	 * The Bag that contains all the elements from self.
	 * 		post: result->forAll(elem | self->includes(elem))
     * 		post: self ->forAll(elem | result->includes(elem))
	 */
	TinkerBag<E> asBag();
}
