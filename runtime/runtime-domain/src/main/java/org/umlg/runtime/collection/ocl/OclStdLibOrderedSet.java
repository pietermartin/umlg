package org.umlg.runtime.collection.ocl;

import org.umlg.runtime.collection.UmlgBag;
import org.umlg.runtime.collection.UmlgOrderedSet;
import org.umlg.runtime.collection.UmlgSequence;

import java.util.Comparator;


public interface OclStdLibOrderedSet<E> extends OclStdLibCollection<E> {

	/**
	 * append (object: T) : OrderedSet(T)
	 * <pre>
	 * The set of elements, consisting of all elements of self, followed by object.
	 * 		post: result->size() = self->size() + 1
	 * 		post: result->at(result->size() ) = object
	 * 		post: Sequence{1..self->size() }->forAll(index : Integer |
	 * 		result->at(index) = self ->at(index))
	 * </pre>
	 * @param e
	 * @return
	 */
	UmlgOrderedSet<E> append(E e);

	/**
	 * prepend(object : T) : OrderedSet(T)
	 * <pre>
	 * The sequence consisting of object, followed by all elements in self.
	 * 		post: result->size = self->size() + 1
	 * 		post: result->at(1) = object
	 * 		post: Sequence{1..self->size()}->forAll(index : Integer |
	 * 		self->at(index) = result->at(index + 1))
	 * </pre>
	 * @param e
	 * @return
	 */
	UmlgOrderedSet<E> prepend(E e);
	
	/**
	 * insertAt(index : Integer, object : T) : OrderedSet(T)
	 * <pre>
	 * The set consisting of self with object inserted at position index.
	 * 		post: result->size = self->size() + 1
	 * 		post: result->at(index) = object
	 * 		post: Sequence{1..(index - 1)}->forAll(i : Integer |
	 * 		self->at(i) = result->at(i))
	 * 		post: Sequence{(index + 1)..self->size()}->forAll(i : Integer |
	 * 		self->at(i) = result->at(i + 1))
	 * </pre>
	 */
	UmlgOrderedSet<E> insertAt(Integer index, E e);
	
	/**
	 * subOrderedSet(lower : Integer, upper : Integer) : OrderedSet(T)
	 * <pre>
	 * The sub-set of self starting at number lower, up to and including element number upper.
	 * 		pre : 1 <= lower
	 * 		pre : lower <= upper
	 * 		pre : upper <= self->size()
	 * 		post: result->size() = upper -lower + 1
	 * 		post: Sequence{lower..upper}->forAll( index |
	 * 		result->at(index - lower + 1) =
	 * 		self->at(index))</pre>
	 */
	UmlgOrderedSet<E> subOrderedSet(Integer lower, Integer upper);
	
	/**
	 * at(i : Integer) : T
	 * <pre>
	 * The i-th element of self.
	 * 		pre : i >= 1 and i <= self->size()
	 * </pre>
	 */
	E at(Integer i);
	
	/**
	 * indexOf(obj : T) : Integer
	 * </pre>
	 * The index of object obj in the sequence.
	 * 		pre : self->includes(obj)
	 * 		post : self->at(i) = obj</pre>
	 */
	//The obj parameter id Object and not E to be compatible with the java.util.List interface
	int indexOf(Object obj);
	
	/**
	 * first() : T
	 * <pre>
	 * The first element in self.
	 * 		post: result = self->at(1)</pre>
	 */
	E first();
	
	/**
	 * last() : T
	 * <pre>
	 * 		The last element in self.
	 * 		post: result = self->at(self->size() )
	 * </pre>
	 */
	E last();
	
	/**
	 * reverse() : OrderedSet(T)
	 * <pre>
	 * The ordered set of elements with same elements but with the opposite order.
	 * 		post: result->size() = self->size()</pre>
	 */
	UmlgOrderedSet<E> reverse();
	
	/**
	 * sum() : T
	 * <pre>
	 * Redefines the Collection operation to remove the requirement for the + operation to be associative and/or commutative,
	 * since the order of evaluation is well-defined by the iteration over an ordered collection.</pre>
	 */
	E sum();
	
//	/**
//	 * asSet() : Set(T)
//	 * </pre>
//	 * Redefines the Set operation. Returns a Set containing all of the elements of self, in undefined order.
//	 * <pre>
//	 */
//	@Override
//	UmlgSet<E> asSet();
	
	/**
	 * asOrderedSet() : OrderedSet(T)
	 * <pre>
	 * 	Redefines the Set operation. An OrderedSet identical to self.
	 * 		post: result = self
	 * 		post: Sequence{1..self.size()}->forAll(i | result->at(i) = self->at(i))</pre>
	 */
	@Override
    UmlgOrderedSet<E> asOrderedSet();
	
	/**
	 * asSequence() : Sequence(T)
	 * <pre>
	 * 	Redefines the Set operation. A Sequence that contains all the elements from self, in the same order.
	 * 		post: Sequence{1..self.size()}->forAll(i | result->at(i) = self->at(i))</pre>
	 * 
	 */
	@Override
    UmlgSequence<E> asSequence();
	
	/**
	 * asBag() : Bag(T)
	 * <pre>
	 * Redefines the Set operation. The Bag that contains all the elements from self, in undefined order.
	 * </pre>
	 */
	@Override
    UmlgBag<E> asBag();
	
	@Override
    UmlgOrderedSet<E> select(BooleanExpressionEvaluator<E> e);
	
	@Override
	<R> UmlgSequence<R> collectNested(BodyExpressionEvaluator<R, E> e);
	
	@Override
	<T, R> UmlgSequence<T> collect(BodyExpressionEvaluator<R, E> e);

    @Override
    <T2> UmlgSequence<T2> flatten();

    /*************************************************
     *
     * Not in spec
     *
     *************************************************/
	/**
	 * including(object : T) : Set(T)
	 * 
	 * <pre>
	 * The set containing all elements of self plus object.
	 * 		post: result->forAll(elem | self->includes(elem) or (elem = object))
	 * 		post: self- >forAll(elem | result->includes(elem)) 
	 * 		post: result->includes(object)
	 * </pre>
	 */
	UmlgOrderedSet<E> including(E e);


	//Predefined Iterator Expressions
	UmlgOrderedSet<E> sortedBy(Comparator<E> e);
}
