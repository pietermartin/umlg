package org.umlg.runtime.collection.memory;

import org.apache.commons.collections4.set.ListOrderedSet;
import org.umlg.runtime.collection.UmlgOrderedSet;
import org.umlg.runtime.collection.UmlgSequence;
import org.umlg.runtime.collection.ocl.BodyExpressionEvaluator;
import org.umlg.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.umlg.runtime.collection.ocl.OclStdLibOrderedSet;
import org.umlg.runtime.collection.ocl.OclStdLibOrderedSetImpl;

import java.util.*;

public class UmlgMemoryOrderedSet<E> extends UmlgMemoryCollection<E> implements UmlgOrderedSet<E> {

	protected OclStdLibOrderedSet<E> oclStdLibOrderedSet;

	@SuppressWarnings("unchecked")
	public UmlgMemoryOrderedSet() {
		super();
		this.internalCollection = new ListOrderedSet<>();
		this.oclStdLibOrderedSet = new OclStdLibOrderedSetImpl<>((ListOrderedSet) this.internalCollection);
		this.oclStdLibCollection = this.oclStdLibOrderedSet;
	}
	
	@SuppressWarnings("unchecked")
	public UmlgMemoryOrderedSet(Collection<E> c) {
		super();
		this.internalCollection = ListOrderedSet.listOrderedSet(new ArrayList<>(c));
		this.oclStdLibOrderedSet = new OclStdLibOrderedSetImpl<>((ListOrderedSet) this.internalCollection);
		this.oclStdLibCollection = this.oclStdLibOrderedSet;
	}
	
	private ListOrderedSet getInternalListOrderedSet() {
		return (ListOrderedSet) this.internalCollection;
	}

	@Override
	public UmlgOrderedSet<E> select(BooleanExpressionEvaluator<E> e) {
		return this.oclStdLibOrderedSet.select(e);
	}

	@Override
	public <T, R> UmlgSequence<T> collect(BodyExpressionEvaluator<R, E> e) {
		return this.oclStdLibOrderedSet.collect(e);
	}

	@Override
	public <R> UmlgSequence<R> collectNested(BodyExpressionEvaluator<R, E> e) {
		return this.oclStdLibOrderedSet.collectNested(e);
	}

    @Override
    public <T2> UmlgSequence<T2> flatten() {
        return this.oclStdLibOrderedSet.flatten();
    }

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		return getInternalListOrderedSet().addAll(c);
	}

	@SuppressWarnings("unchecked")
	@Override
	public E get(int index) {
		return (E) getInternalListOrderedSet().get(index);
	}

	@Override
	public E set(int index, E element) {
		E removedElement = this.remove(index);
		getInternalListOrderedSet().add(index, element);
		return removedElement;
	}

	@Override
	public void add(int index, E element) {
		getInternalListOrderedSet().add(index, element);
	}

	@SuppressWarnings("unchecked")
	@Override
	public E remove(int index) {
		return (E) getInternalListOrderedSet().remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return getInternalListOrderedSet().indexOf(o);
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
	public UmlgOrderedSet<E> append(E e) {
		return this.oclStdLibOrderedSet.append(e);
	}

	@Override
	public UmlgOrderedSet<E> prepend(E e) {
		return this.oclStdLibOrderedSet.prepend(e);
	}

	@Override
	public UmlgOrderedSet<E> insertAt(Integer index, E e) {
		return this.oclStdLibOrderedSet.insertAt(index, e);
	}

	@Override
	public UmlgOrderedSet<E> subOrderedSet(Integer lower, Integer upper) {
		return this.oclStdLibOrderedSet.subOrderedSet(lower, upper);
	}

	@Override
	public E at(Integer i) {
		return this.oclStdLibOrderedSet.at(i);
	}

	@Override
	public E first() {
		return this.oclStdLibOrderedSet.first();
	}

	@Override
	public E last() {
		return this.oclStdLibOrderedSet.last();
	}

	@Override
	public UmlgOrderedSet<E> reverse() {
		return this.oclStdLibOrderedSet.reverse();
	}

	@Override
	public UmlgOrderedSet<E> including(E e) {
		return this.oclStdLibOrderedSet.including(e);
	}

	@Override
	public UmlgOrderedSet<E> sortedBy(Comparator<E> comparator) {
		return this.oclStdLibOrderedSet.sortedBy(comparator);
	}
}
