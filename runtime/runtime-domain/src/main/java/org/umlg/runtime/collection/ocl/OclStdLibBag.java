package org.umlg.runtime.collection.ocl;

import org.umlg.runtime.collection.TinkerBag;
import org.umlg.runtime.collection.TinkerOrderedSet;
import org.umlg.runtime.collection.TinkerSequence;
import org.umlg.runtime.collection.TinkerSet;


public interface OclStdLibBag<E> extends OclStdLibCollection<E> {

	/**
	 * = (bag : Bag(T)) : Boolean
	 * <pre>
	 * True if self and bag contain the same elements, the same number of times.
	 * 		post: result = (self->forAll(elem | self->count(elem) = bag->count(elem)) and
	 * 		bag->forAll(elem | bag->count(elem) = self->count(elem)) )</pre>
	 */
	Boolean equals(TinkerBag<E> bag);
	
	/**
	 * union(bag : Bag(T)) : Bag(T)
	 * <pre>
	 * The union of self and bag.
	 * 		post: result->forAll( elem | result->count(elem) = self->count(elem) + bag->count(elem))
	 * 		post: self ->forAll( elem | result->count(elem) = self->count(elem) + bag->count(elem))
	 * 		post: bag ->forAll( elem | result->count(elem) = self->count(elem) + bag->count(elem))
	 * </pre>
	 */
	TinkerBag<E> union(TinkerBag<E> bag);
	
	/**
	 * union(set : Set(T)) : Bag(T)
	 * <pre>
	 * The union of self and set.
	 * 		post: result->forAll(elem | result->count(elem) = self->count(elem) + set->count(elem))
	 * 		post: self ->forAll(elem | result->count(elem) = self->count(elem) + set->count(elem))
	 * 		post: set ->forAll(elem | result->count(elem) = self->count(elem) + set->count(elem))
	 * </pre>
	 */
	TinkerBag<E> union(TinkerSet<E> set);
	
	/**
	 * intersection(bag : Bag(T)) : Bag(T)
	 * <pre>
	 * The intersection of self and bag.
	 * 		post: result->forAll(elem | result->count(elem) = self->count(elem).min(bag->count(elem)) )
	 * 		post: self->forAll(elem | result->count(elem) = self->count(elem).min(bag->count(elem)) )
	 * 		post: bag->forAll(elem | result->count(elem) = self->count(elem).min(bag->count(elem)) )
	 * </pre>
	 */
	TinkerBag<E> intersection(TinkerBag<E> bag);
	
	/**
	 * intersection(bag : Bag(T)) : Bag(T)
     *  The intersection of self and bag.
     *      post: result->forAll(elem | result->count(elem) = self->count(elem).min(bag->count(elem)) )
     *      post: self->forAll(elem | result->count(elem) = self->count(elem).min(bag->count(elem)) )
     *      post: bag->forAll(elem | result->count(elem) = self->count(elem).min(bag->count(elem)) )
	 */
	TinkerSet<E> intersection(TinkerSet<E> set);
	
	/**
	 * including(object : T) : Bag(T)
	 * <pre>
	 * The bag containing all elements of self plus object.
	 * 		post: result->forAll(elem |
	 * 			if elem = object then
	 * 				result->count(elem) = self->count(elem) + 1
	 * 			else
	 * 				result->count(elem) = self->count(elem)
	 * 			endif)
	 * 	 	post: self->forAll(elem |
	 * 			if elem = object then
	 * 				result->count(elem) = self->count(elem) + 1
	 * 			else
	 * 				result->count(elem) = self->count(elem)
	 * 			endif)
	 * </pre>
	 */
	TinkerBag<E> including(E object);
	
	/**
	 * excluding(object : T) : Bag(T)
	 * <pre>
	 * The bag containing all elements of self apart from all occurrences of object.
	 * 		post: result->forAll(elem |
	 * 			if elem = object then
	 * 				result->count(elem) = 0
	 * 			else
	 * 				result->count(elem) = self->count(elem)
	 * 			endif)
	 * 		post: self->forAll(elem |
	 * 			if elem = object then
	 * 				result->count(elem) = 0
	 * 			else
	 * 				result->count(elem) = self->count(elem)
	 * 			endif)
	 * </pre>
	 */
	TinkerBag<E> excluding(E object);
	
	/**
	 * count(object : T) : Integer
	 * <pre>
	 * The number of occurrences of object in self.
	 * </pre>
	 */
	@Override
	int count(E object);
	
	/**
	 * asBag() : Bag(T)
	 * <pre>
	 * Redefines the Collection operation. A Bag identical to self. This operation exists for convenience reasons.
	 * 		post: result = self
	 * </pre>
	 */
	@Override
	TinkerBag<E> asBag();
	
	/**
	 * asSequence() : Sequence(T)
	 * <pre>
	 * Redefines the Collection operation. A Sequence that contains all the elements from self, in undefined order.
	 * 		post: result->forAll(elem | self->count(elem) = result->count(elem))
	 * 		post: self ->forAll(elem | self->count(elem) = result->count(elem))
	 * </pre>
	 */
	@Override
	TinkerSequence<E> asSequence();
	
//	/**
//	 * asSet() : Set(T)
//	 * <pre>
//	 * Redefines the Collection operation. The Set containing all the elements from self, with duplicates removed.
//	 * 		post: result->forAll(elem | self ->includes(elem))
//	 * 		post: self ->forAll(elem | result->includes(elem))
//	 * </pre>
//	 */
//	@Override
//	<E> TinkerSet<E> asSet();

	/**
	 * asOrderedSet() : OrderedSet(T)
	 * <pre>
	 * Redefines the Collection operation. An OrderedSet that contains all the elements from self, in undefined order, with
	 * duplicates removed.
	 * 		post: result->forAll(elem | self ->includes(elem))
	 * 		post: self ->forAll(elem | result->includes(elem))
	 * 		post: self ->forAll(elem | result->count(elem) = 1)
	 * </pre>
	 */
	TinkerOrderedSet<E> asOrderedSet();
	
	/***************************************************
	 * Iterate goodies
	 ***************************************************/
	
	@Override
	TinkerBag<E> select(BooleanExpressionEvaluator<E> e);
	
	@Override
	<R> TinkerBag<R> collectNested(BodyExpressionEvaluator<R, E> e);

	@Override
	<T, R> TinkerBag<T> collect(BodyExpressionEvaluator<R, E> e);

    /**
     * flatten() : Bag(T2)
     * <pre>
     * Redefines the Collection operation. If the element type is not a collection type, this results in the same bag as self. If the
     * element type is a collection type, the result is the bag containing all the elements of all the recursively flattened elements
     * of self.
     * 		post: result = if self.oclType().elementType.oclIsKindOf(CollectionType) then
     * 			self->iterate(c; acc : Bag(T2) = Bag{} |
     * 				acc->union(c->flatten()->asBag() ) )
     * 			else
     * 				self
     * 			endif
     * </pre>
     */
    @Override
    <T2> TinkerBag<T2> flatten();

}
