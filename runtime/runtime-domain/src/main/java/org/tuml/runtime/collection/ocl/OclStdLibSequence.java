package org.tuml.runtime.collection.ocl;

import org.tuml.runtime.collection.TinkerBag;
import org.tuml.runtime.collection.TinkerOrderedSet;
import org.tuml.runtime.collection.TinkerSequence;


public interface OclStdLibSequence<E> extends OclStdLibCollection<E> {

	/**
	 * count(object : T) : Integer
	 * <pre>
	 * The number of occurrences of object in self.
	 * </pre>
	 *
	 */
	@Override
	int count(E object);

	/**
	 * = (s : Sequence(T)) : Boolean
	 * True if self contains the same elements as s in the same order.
	 * post: result = Sequence{1..self->size()}->forAll(index : Integer |
	 * self->at(index) = s->at(index))
	 * and
	 * self->size() = s->size()
	 */
	Boolean equals(TinkerSequence<E> s);
	
	/**
	 * union (s : Sequence(T)) : Sequence(T)
	 * <pre>
	 * The sequence consisting of all elements in self, followed by all elements in s.
	 * 		post: result->size() = self->size() + s->size()
	 * 		post: Sequence{1..self->size()}->forAll(index : Integer |
	 * 						self->at(index) = result->at(index))
	 * 		post: Sequence{1..s->size()}->forAll(index : Integer |
	 * 						s->at(index) = result->at(index + self->size() )))
	 * </pre>
	 * 
	 */
	TinkerSequence<E> union(TinkerSequence<? extends E> s);
	
	/**
	 * append (object: T) : Sequence(T)
	 * <pre>
	 * The sequence of elements, consisting of all elements of self, followed by object.
	 * 		post: result->size() = self->size() + 1
	 * 		post: result->at(result->size() ) = object
	 * 		post: Sequence{1..self->size() }->forAll(index : Integer |
	 * 			result->at(index) = self ->at(index))
	 * </pre>
	 * @return
	 */
	TinkerSequence<E> append(E object);
	
	/**
	 * prepend(object : T) : Sequence(T)
	 * <pre>
	 * The sequence consisting of object, followed by all elements in self.
	 * 		post: result->size = self->size() + 1
	 * 		post: result->at(1) = object
	 * 		post: Sequence{1..self->size()}->forAll(index : Integer |
	 * 			self->at(index) = result->at(index + 1))
	 * </pre>
	 * @return
	 */
	TinkerSequence<E> prepend(E object);
	
	/**
	 * insertAt(index : Integer, object : T) : Sequence(T)
	 * <pre>
	 * The sequence consisting of self with object inserted at position index.
	 * 		post: result->size = self->size() + 1
	 * 		post: result->at(index) = object
	 * 		post: Sequence{1..(index - 1)}->forAll(i : Integer |
	 * 			self->at(i) = result->at(i))
	 * 		post: Sequence{(index + 1)..self->size()}->forAll(i : Integer |
	 * 			self->at(i) = result->at(i + 1))
	 * </pre>
	 * @return
	 */
	TinkerSequence<E> insertAt(Integer index, E object);
	
	/**
	 * subSequence(lower : Integer, upper : Integer) : Sequence(T)
	 * <pre>
	 * The sub-sequence of self starting at number lower, up to and including element number upper.
	 * 	pre : 1 <= lower
	 * 	pre : lower <= upper
	 * 	pre : upper <= self->size()
	 * 	post: result->size() = upper -lower + 1
	 * 	post: Sequence{lower..upper}->forAll( index |
	 * 		result->at(index - lower + 1) =
	 * 			self->at(index))
	 * </pre>
	 * @return
	 */
	TinkerSequence<E> subSequence(Integer lower, Integer upper);
	
	/**
	 * at(i : Integer) : T
	 * <pre>
	 * 	The i-th element of sequence.
	 * 	pre : i >= 1 and i <= self->size()
	 * </pre>
	 * @return
	 */
	E at(Integer i);
	
	/**
	 * indexOf(obj : T) : Integer
	 * <pre>
	 * The index of object obj in the sequence.
	 * 	pre : self->includes(obj)
	 * 	post : self->at(i) = obj
	 * </pre>
	 * @return
	 */
	//The obj parameter id Object and not E to be compatible with the java.util.List interface
	int indexOf(Object obj);
	
	/**
	 * first() : T
	 * <pre>
	 * The first element in self.
	 * post: result = self->at(1)
	 * </pre>
	 * @return
	 */
	E first();
	
	/**
	 * last() : T
	 * <pre>
	 * The last element in self.
	 * 	post: result = self->at(self->size() )
	 * </pre>
	 */
	E last();
	
	/**
	 * including(object : T) : Sequence(T)
	 * <pre>
	 * The sequence containing all elements of self plus object added as the last element.
	 * 	post: result = self.append(object)
	 * </pre>
	 */
	TinkerSequence<E> including(E object);
	
	/**
	 * excluding(object : T) : Sequence(T)
	 * <pre>
	 * The sequence containing all elements of self apart from all occurrences of object.
	 * The order of the remaining elements is not changed.
	 * 	post:result->includes(object) = false
	 * 	post: result->size() = self->size() - self->count(object)
	 * 	post: result = self->iterate(elem; acc : Sequence(T)
	 * 		= Sequence{}|
	 * 			if elem = object then acc else acc->append(elem) endif )
	 * </pre>
	 */
	TinkerSequence<E> excluding(E object);
	
	/**
	 * reverse() : Sequence(T)
	 * <pre>
	 * The sequence containing the same elements but with the opposite order.
	 * 	post: result->size() = self->size()
	 * </pre>
	 */
	TinkerSequence<E> reverse();
	
	/**
	 * sum() : T
	 * <pre>
	 * Redefines the Collection operation to remove the requirement for the + operation to be associative and/or commutative,
	 * since the order of evaluation is well-defined by the iteration over an ordered collection.
	 * </pre>
	 */
	E sum();
	
	/**
	 * asBag() : Bag(T)
	 * <pre>
	 * Redefines the Collection operation. The Bag containing all the elements from self, including duplicates.
	 * 	post: result->forAll(elem | self->count(elem) = result->count(elem) )
	 * 	post: self->forAll(elem | self->count(elem) = result->count(elem) )
	 * </pre>
	 */
	@Override
	TinkerBag<E> asBag();
	
	/**
	 * asSequence() : Sequence(T)
	 * Redefines the Collection operation. The Sequence identical to the object itself. This operation exists for convenience
	 * reasons.
	 * post: result = self
	 * 
	 */
	@Override
	TinkerSequence<E> asSequence();
	
//	/**
//	 * asSet() : Set(T)
//	 * <pre>
//	 * Redefines the Collection operation. The Set containing all the elements from self, with duplicates removed.
//	 * 	post: result->forAll(elem | self ->includes(elem))
//	 * 	post: self ->forAll(elem | result->includes(elem))
//	 * </pre>
//	 */
//	@Override
//	TinkerSet<E> asSet();
	
	/**
	 * asOrderedSet() : OrderedSet(T)
	 * <pre>
	 * Redefines the Collection operation. An OrderedSet that contains all the elements from self, in the same order, with
	 * duplicates removed.
	 * 	post: result->forAll(elem | self ->includes(elem))
	 * post: self ->forAll(elem | result->includes(elem))
	 * post: self ->forAll(elem | result->count(elem) = 1)
	 * post: self ->forAll(elem1, elem2 |
	 * 		self->indexOf(elem1) < self->indexOf(elem2)
	 * 			implies result->indexOf(elem1) < result->indexOf(elem2) )
	 * </pre>
	 */
	TinkerOrderedSet<E> asOrderedSet();
	
	/***************************************************
	 * Iterate goodies
	 ***************************************************/
	
	@Override
	TinkerSequence<E> select(BooleanExpressionEvaluator<E> e);
	
	@Override
	<R> TinkerSequence<R> collectNested(BodyExpressionEvaluator<R, E> e);
	
	@Override
	<T, R> TinkerSequence<T> collect(BodyExpressionEvaluator<R, E> e);

    /**
     * flatten() : Sequence(T2)
     * <pre>
     * Redefines the Collection operation. If the element type is not a collection type, this results in the same sequence as self.
     * If the element type is a collection type, the result is the sequence containing all the elements of all the recursively
     * flattened elements of self. The order of the elements is partial.
     * 		post: result = if self.oclType().elementType.oclIsKindOf(CollectionType) then
     * 			self->iterate(c; acc : Sequence(T2) = Sequence{} |
     * 				acc->union(c->flatten()->asSequence() ) )
     * 			else
     * 				self
     * 			endif
     * </pre>
     *
     */
    @Override
    <T2> TinkerSequence<T2> flatten();

}
