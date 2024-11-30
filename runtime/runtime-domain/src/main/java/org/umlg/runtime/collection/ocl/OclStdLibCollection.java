package org.umlg.runtime.collection.ocl;

import org.umlg.runtime.collection.*;
import org.umlg.runtime.domain.ocl.OclAny;

import java.util.Comparator;

/**
 * From ocl spec v2.3.1
 */
public interface OclStdLibCollection<E> extends OclAny, Iterable<E> {

    default String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int count = 0;
        for (E e : this) {
            count++;
            if (e instanceof String) {
                sb.append("\"");
                sb.append(e);
                sb.append("\"");
            } else if (e instanceof Enum) {
                sb.append("\"");
                sb.append(e);
                sb.append("\"");
            } else {
                sb.append(e);
            }
            if (count < size()) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * = (c : Collection(T)) : Boolean
     * <pre>
     * True if c is a collection of the same kind as self and contains the same
     * elements in the same quantities and in the same order, in the case of an
     * ordered collection type.<pre>
     */
    boolean equals(UmlgCollection<E> c);

    /**
     * <> (c : Collection(T)) : Boolean
     * <pre>
     * True if c is not equal to self.
     * 	post: result = not (self = c)
     * </pre>
     */
    boolean notEquals(UmlgCollection<E> c);

    /**
     * size() : Integer
     * <pre>
     * The number of elements in the collection self.
     * post: result = self->iterate(elem; acc : Integer = 0 | acc + 1)
     * </pre>
     *
     * @return int
     */
    int size();

    /**
     * includes(object : T) : Boolean
     * <pre>
     * True if object is an element of self, false otherwise.
     * post: result = (self->count(object) > 0)
     * </pre>
     */
    boolean includes(E t);

    /**
     * excludes(object : T) : Boolean
     * <pre>
     * True if object is not an element of self, false otherwise.
     * post: result = (self->count(object) = 0)
     * </pre>
     */
    boolean excludes(E t);

    /**
     * count(object : T) : Integer
     * <pre>
     * The number of times that object occurs in the collection self.
     * 	post: result = self->iterate( elem; acc : Integer = 0 |
     * 		if elem = object then acc + 1 else acc endif)
     * </pre>
     */
    int count(E e);

    /**
     * includesAll(c2 : Collection(T)) : Boolean
     * <pre>
     * Does self contain all the elements of c2 ?
     * post: result = c2->forAll(elem | self->includes(elem))
     * </pre>
     *
     * @param c
     * @return
     */
    Boolean includesAll(UmlgCollection<E> c);

    /**
     * excludesAll(c2 : Collection(T)) : Boolean
     * <pre>
     * Does self contain none of the elements of c2 ?
     * post: result = c2->forAll(elem | self->excludes(elem))
     * </pre>
     *
     * @param c
     * @return
     */
    Boolean excludesAll(UmlgCollection<E> c);

    /**
     * isEmpty() : Boolean
     * <pre>
     * Is self the empty collection?
     * 	post: result = ( self->size() = 0 )
     * Note: null->isEmpty() returns 'true' in virtue of the implicit casting from null to Bag{}
     * </pre>
     *
     * @return
     */
    boolean isEmpty();

    /**
     * notEmpty() : Boolean
     * <pre>
     * Is self not the empty collection?
     * 	post: result = ( self->size() <> 0 )
     * null->notEmpty() returns 'false' in virtue of the implicit casting from null to Bag{}.
     * </pre>
     *
     * @return
     */
    Boolean notEmpty();

    /**
     * max() : T
     * <pre>
     * The element with the maximum value of all elements in self. Elements must be of a type supporting the max operation.
     * The max operation - supported by the elements - must take one parameter of type T and be both associative and
     * commutative. UnlimitedNatural, Integer and Real fulfill this condition.
     * 	post: result = self->iterate( elem; acc : T = self.first() | acc.max(elem) )
     * </pre>
     *
     * @return
     */
    E max();

    /**
     * min() : T
     * <pre>
     * The element with the minimum value of all elements in self. Elements must be of a type supporting the min operation.
     * The min operation - supported by the elements - must take one parameter of type T and be both associative and
     * commutative. UnlimitedNatural, Integer and Real fulfill this condition.
     * 	post: result = self->iterate( elem; acc : T = self.first() | acc.min(elem) )
     * </pre>
     *
     * @return
     */
    E min();

    /**
     * sum() : T
     * <pre>
     * The addition of all elements in self. Elements must be of a type supporting the + operation. The + operation must take one
     * parameter of type T and be both associative: (a+b)+c = a+(b+c), and commutative: a+b = b+a. UnlimitedNatural, Integer
     * and Real fulfill this condition.
     * 	post: result = self->iterate( elem; acc : T = 0 | acc + elem )
     * If the + operation is not both associative and commutative, the sum expression is not well-formed, which may result in
     * unpredictable results during evaluation. If an implementation is able to detect a lack of associativity or commutativity, the
     * implementation may bypass the evaluation and return an invalid result.
     * <pre>
     *
     * @return
     */
    E sum();

    /**
     * product(c2: Collection(T2)) : Set( Tuple( first: T, second: T2) )
     * <pre>
     * The cartesian product operation of self and c2.
     * 	post: result = self->iterate (e1; acc: Set(Tuple(first: T, second: T2)) = Set{} |
     * 		c2->iterate (e2; acc2: Set(Tuple(first: T, second: T2)) = acc |
     * 			acc2->including (Tuple{first = e1, second = e2}) ) )
     * </pre>
     *
     * @return
     */
    // TODO support Tuples
    UmlgSet<?> product(UmlgCollection<E> c);

    /**
     * asSet() : Set(T)
     * <pre>
     * The Set containing all the elements from self, with duplicates removed.
     * 	post: result->forAll(elem | self ->includes(elem))
     * 	post: self ->forAll(elem | result->includes(elem))
     * </pre>
     *
     * @return
     */
    <E> UmlgSet<E> asSet();

    /**
     * asOrderedSet() : OrderedSet(T)
     * <pre>
     * An OrderedSet that contains all the elements from self, with duplicates removed, in an order dependent on the particular
     * concrete collection type.
     * 	post: result->forAll(elem | self->includes(elem))
     * 	post: self ->forAll(elem | result->includes(elem))
     * </pre>
     */
    UmlgOrderedSet<E> asOrderedSet();

    /**
     * asSequence() : Sequence(T)
     * <pre>
     * A Sequence that contains all the elements from self, in an order dependent on the particular concrete collection type.
     * 	post: result->forAll(elem | self->includes(elem))
     * 	post: self ->forAll(elem | result->includes(elem))
     * </pre>
     */
    UmlgSequence<E> asSequence();

    /**
     * asBag() : Bag(T)
     * <pre>
     * The Bag that contains all the elements from self.
     * 	post: result->forAll(elem | self->includes(elem))
     * 	post: self ->forAll(elem | result->includes(elem))
     * </pre>
     */
    UmlgBag<E> asBag();

    /***************************************************
     * Iterate goodies
     ***************************************************/

    <R> R iterate(IterateExpressionAccumulator<R, E> v);

    /**
     * The subset of set for which expr is true. source->select(iterator | body)
     * = source->iterate(iterator; result : Set(T) = Set{} | if body then
     * result->including(iterator) else result endif) select may have at most
     * one iterator variable.
     */
    UmlgCollection<E> select(BooleanExpressionEvaluator<E> e);

    /**
     * Returns any element in the source collection for which body evaluates to
     * true. If there is more than one element for which body is true, one of
     * them is returned. There must be at least one element fulfilling body,
     * otherwise the result of this IteratorExp is null.
     * <p>
     * source->any(iterator | body) = source->select(iterator |
     * body)->asSequence()->first()
     * <p>
     * any may have at most one iterator variable.
     *
     * @param e
     * @return
     */
    E any(BooleanExpressionEvaluator<E> e);

    /**
     * Results in invalid if if body evaluates to invalid for any element in the source collection,
     * otherwise true if body evaluates to a different, possibly null, value for each element in the source collection;
     * otherwise result is false.
     * <p>
     * source->isUnique (iterator | body) =
     * source->collect (iterator | Tuple{iter = Tuple{iterator}, value = body})
     * ->forAll (x, y | (x.iter <> y.iter) implies (x.value <> y.value))
     * <p>
     * isUnique may have at most one iterator variable.
     */
    <R> Boolean isUnique(BodyExpressionEvaluator<R, E> e);

    Boolean exists(BooleanExpressionEvaluator<E> e);

    Boolean forAll(BooleanExpressionEvaluator<E> e);

    /**
     * The Collection of elements that results from applying body to every
     * member of the source set. The result is flattened. Notice that this is
     * based on collectNested, which can be of different type depending on the
     * type of source. collectNested is defined individually for each subclass
     * of CollectionType.
     * <p>
     * source->collect (iterator | body) = source->collectNested (iterator |
     * body)->flatten()
     * <p>
     * collect may have at most one iterator variable.
     *
     * @param e
     * @return
     */
    <T, R> UmlgCollection<T> collect(BodyExpressionEvaluator<R, E> e);

    /**
     * The Bag of elements which results from applying body to every member of
     * the source set. source->collectNested(iterator | body) =
     * source->iterate(iterator; result : Bag(body.type) = Bag{} |
     * result->including(body ) )
     * <p>
     * collectNested may have at most one iterator variable.
     *
     * @param e
     * @return
     */
    <R> UmlgCollection<R> collectNested(BodyExpressionEvaluator<R, E> e);

    /**
     * flatten() : Collection(T2)
     * <pre>
     * If the element type is not a collection type, this results in the same collection as self. If the element type is a collection
     * type, the result is a collection containing all the elements of all the recursively flattened elements of self.
     * [1] Well-formedness rules
     * [2] [1] A collection cannot contain invalid values.
     * 		context Collection
     * 		inv: self->forAll(not oclIsInvalid())
     * </pre>
     *
     * @return
     */
    <T2> UmlgCollection<T2> flatten();


    //Predefined Iterator Expressions
    UmlgCollection<E> sortedBy(Comparator<E> e);

}
