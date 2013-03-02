package org.tuml.runtime.collection.persistent;

import org.apache.commons.collections.set.ListOrderedSet;
import org.tuml.runtime.collection.TinkerOrderedSet;
import org.tuml.runtime.collection.TinkerSequence;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.collection.ocl.BodyExpressionEvaluator;
import org.tuml.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.tuml.runtime.collection.ocl.OclStdLibOrderedSet;
import org.tuml.runtime.collection.ocl.OclStdLibOrderedSetImpl;
import org.tuml.runtime.domain.TumlNode;

import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;

/**
 * Date: 2013/03/02
 * Time: 9:18 PM
 */
public abstract class TumlBaseOrderedSet<E> extends BaseCollection<E> implements TinkerOrderedSet<E> {

    protected OclStdLibOrderedSet<E> oclStdLibOrderedSet;

    @SuppressWarnings("unchecked")
    public TumlBaseOrderedSet(TumlNode owner, TumlRuntimeProperty runtimeProperty) {
        super(owner, runtimeProperty);
        this.internalCollection = new ListOrderedSet();
        this.oclStdLibOrderedSet = new OclStdLibOrderedSetImpl<E>((ListOrderedSet) this.internalCollection);
        this.oclStdLibCollection = this.oclStdLibOrderedSet;
    }

    protected ListOrderedSet getInternalListOrderedSet() {
        return (ListOrderedSet) this.internalCollection;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E get(int index) {
        if (!this.loaded) {
            loadFromVertex();
        }
        return (E) this.getInternalListOrderedSet().get(index);
    }

    @Override
    public E remove(int index) {
        E e = this.get(index);
        this.remove(e);
        return e;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clear() {
        maybeLoad();
        for (E e : new HashSet<E>(this.getInternalListOrderedSet())) {
            this.remove(e);
        }
    }

    @Override
    public int indexOf(Object o) {
        maybeLoad();
        return this.getInternalListOrderedSet().indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new RuntimeException("Not supported");
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public <R> TinkerSequence<R> collectNested(BodyExpressionEvaluator<R, E> v) {
        maybeLoad();
        return this.oclStdLibOrderedSet.collectNested(v);
    }

    @Override
    public <T, R> TinkerSequence<T> collect(BodyExpressionEvaluator<R, E> v) {
        maybeLoad();
        return this.oclStdLibOrderedSet.collect(v);
    }

    @Override
    public <T2> TinkerSequence<T2> flatten() {
        maybeLoad();
        return this.oclStdLibOrderedSet.flatten();
    }

    @Override
    public TinkerOrderedSet<E> select(BooleanExpressionEvaluator<E> v) {
        maybeLoad();
        return this.oclStdLibOrderedSet.select(v);
    }

    @Override
    public TinkerOrderedSet<E> append(E e) {
        maybeLoad();
        return this.oclStdLibOrderedSet.append(e);
    }

    @Override
    public TinkerOrderedSet<E> prepend(E e) {
        maybeLoad();
        return this.oclStdLibOrderedSet.prepend(e);
    }

    @Override
    public TinkerOrderedSet<E> insertAt(Integer index, E e) {
        maybeLoad();
        return this.oclStdLibOrderedSet.insertAt(index, e);
    }

    @Override
    public TinkerOrderedSet<E> subOrderedSet(Integer lower, Integer upper) {
        maybeLoad();
        return this.oclStdLibOrderedSet.subOrderedSet(lower, upper);
    }

    @Override
    public E at(Integer i) {
        maybeLoad();
        return this.oclStdLibOrderedSet.at(i);
    }

    @Override
    public E first() {
        maybeLoad();
        return this.oclStdLibOrderedSet.first();
    }

    @Override
    public E last() {
        maybeLoad();
        return this.oclStdLibOrderedSet.last();
    }

    @Override
    public TinkerOrderedSet<E> reverse() {
        maybeLoad();
        return this.oclStdLibOrderedSet.reverse();
    }

    @Override
    public TinkerOrderedSet<E> including(E e) {
        maybeLoad();
        return this.oclStdLibOrderedSet.including(e);
    }


}
