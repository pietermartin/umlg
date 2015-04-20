package org.umlg.runtime.collection.ocl;

import org.umlg.runtime.collection.*;

import java.util.Comparator;

public interface OclStdLibSet<E> extends OclStdLibCollection<E> {

	/**
	 * union(s : Set(T)) : Set(T)
	 * 
	 * <pre>
	 * The union of self and s.
	 * 		post: result->forAll(elem | self->includes(elem) or s->includes(elem))
	 * 		post: self ->forAll(elem | result->includes(elem))
	 * post: s ->forAll(elem | result->includes(elem))
	 * 
	 * <pre>
	 */
	UmlgSet<E> union(UmlgSet<? extends E> s);

	/**
	 * union(bag : Bag(T)) : Bag(T)
	 * 
	 * <pre>
	 * The union of self and bag.
	 * 		post: result->forAll(elem | result->count(elem) = self->count(elem) + bag->count(elem))
	 * 		post: self->forAll(elem | result->includes(elem))
	 * 		post: bag ->forAll(elem | result->includes(elem))
	 * </pre>
	 */
	UmlgBag<E> union(UmlgBag<? extends E> bag);

	/**
	 * = (s : Set(T)) : Boolean
	 * 
	 * <pre>
	 * Evaluates to true if self and s contain the same elements.
	 * 		post: result = (self->forAll(elem | s->includes(elem)) and
	 * s->forAll(elem | self->includes(elem)) )
	 * 
	 * <pre>
	 */
	Boolean equals(UmlgSet<E> s);

	/**
	 * intersection(s : Set(T)) : Set(T)
	 * 
	 * <pre>
	 * 	The intersection of self and s (i.e., the set of all elements that are in both self and s).
	 * 		post: result->forAll(elem | self->includes(elem) and s->includes(elem))
	 * 		post: self->forAll(elem | s ->includes(elem) = result->includes(elem))
	 * post: s ->forAll(elem | self->includes(elem) = result->includes(elem))
	 * 
	 * <pre>
	 */
	UmlgSet<E> intersection(UmlgSet<E> s);

	/**
	 * intersection(bag : Bag(T)) : Set(T)
	 * 
	 * <pre>
	 * The intersection of self and bag.
	 * 		post: result = self->intersection( bag->asSet )
	 * </pre>
	 */
	UmlgSet<E> intersection(UmlgBag<E> bag);

	/**
	 * – (s : Set(T)) : Set(T)
	 * 
	 * <pre>
	 * The elements of self, which are not in s.
	 * post: result->forAll(elem | self->includes(elem) and s->excludes(elem))
	 * </pre>
	 */
	UmlgSet<E> subtract(UmlgSet<E> s);

	/**
	 * including(object : T) : Set(T)
	 * 
	 * <pre>
	 * The set containing all elements of self plus object.
	 * 		post: result->forAll(elem | self->includes(elem) or (elem = object))
	 * 		post: self- >forAll(elem | result->includes(elem)) post:
	 * 		result->includes(object)
	 * </pre>
	 */
	UmlgSet<E> including(E e);

	/**
	 * excluding(object : T) : Set(T)
	 * 
	 * <pre>
	 * The set containing all elements of self without object. 
	 * 		post:result->forAll(elem | self->includes(elem) and (elem <> object)) 
	 * 		post:self- >forAll(elem | result->includes(elem) = (object <> elem)) 
	 * 		post:result->excludes(object)
	 * </pre>
	 */
	UmlgSet<E> excluding(E e);

	/**
	 * symmetricDifference(s : Set(T)) : Set(T)
	 * 
	 * <pre>
	 * The sets containing all the elements that are in self or s, but not in both. 
	 * 		post: result->forAll(elem | self->includes(elem) xor s->includes(elem)) 
	 * 		post: self->forAll(elem | result->includes(elem) = s ->excludes(elem)) 
	 * 		post: s ->forAll(elem | result->includes(elem) = self->excludes(elem))
	 * </pre>
	 * 
	 */
	UmlgSet<E> symmetricDifference(UmlgSet<E> s);

	/**
	 * count(object : T) : Integer
	 * 
	 * <pre>
	 * The number of occurrences of object in self.
	 * 		post: result <= 1
	 * </pre>
	 */
	int count(E e);

//	/**
//	 * asSet() : Set(T)
//	 * 
//	 * <pre>
//	 * Redefines the Collection operation. A Set identical to self. This operation exists for convenience reasons.
//	 * 		post: result = self
//	 * </pre>
//	 */
//	@Override
//	UmlgSet<E> asSet();

	/**
	 * asOrderedSet() : OrderedSet(T)
	 * 
	 * <pre>
	 * Redefines the Collection operation. An OrderedSet that contains all the elements from self, in undefined order.
	 * 		post: result->forAll(elem | self->includes(elem))
	 * </pre>
	 */
	@Override
    UmlgOrderedSet<E> asOrderedSet();

	/**
	 * asSequence() : Sequence(T)
	 * 
	 * <pre>
	 * Redefines the Collection operation. A Sequence that contains all the elements from self, in undefined order.
	 * 		post: result->forAll(elem | self->includes(elem))
	 * 		post: self->forAll(elem | result->count(elem) = 1)
	 * </pre>
	 */
	@Override
    UmlgSequence<E> asSequence();

	/**
	 * asBag() : Bag(T)
	 * <pre>
	 * Redefines the Collection operation. The Bag that contains all the elements from self.
	 * 		post: result->forAll(elem | self->includes(elem))
	 * 		post: self->forAll(elem | result->count(elem) = 1)
	 * </pre>
	 */
	@Override
    UmlgBag<E> asBag();
	
	/***************************************************
	 * Iterate goodies
	 ***************************************************/
	
	@Override
    UmlgSet<E> select(BooleanExpressionEvaluator<E> e);

	@Override
	<R> UmlgBag<R> collectNested(BodyExpressionEvaluator<R, E> e);

	@Override
	<T, R> UmlgBag<T> collect(BodyExpressionEvaluator<R, E> e);

    /**
     * flatten() : Set(T2)
     *
     * <pre>
     * Redefines the Collection operation. If the element type is not a collection type, this results in the same set as self. If the
     * element type is a collection type, the result is the set containing all the elements of all the recursively flattened elements
     * of self.
     * 		post: result = if self.oclType().elementType.oclIsKindOf(CollectionType) then
     * 						self->iterate(c; acc : Set(T2) = Set{} |
     * 							acc->union(c->flatten()->asSet() ) )
     * 						else
     * 							self
     * 						endif
     * </pre>
     */
    @Override
    <R> UmlgSet<R> flatten();

	//Predefined Iterator Expressions
	@Override
	UmlgOrderedSet<E> sortedBy(Comparator<E> e);
}
