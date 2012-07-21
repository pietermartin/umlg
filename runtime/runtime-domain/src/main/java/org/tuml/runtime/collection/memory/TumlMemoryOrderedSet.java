package org.tuml.runtime.collection.memory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.set.ListOrderedSet;
import org.tuml.runtime.collection.TinkerBag;
import org.tuml.runtime.collection.TinkerOrderedSet;
import org.tuml.runtime.collection.ocl.BodyExpressionEvaluator;
import org.tuml.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.tuml.runtime.collection.ocl.OclStdLibOrderedSet;
import org.tuml.runtime.collection.ocl.OclStdLibOrderedSetImpl;

public class TumlMemoryOrderedSet<E> extends TumlMemoryCollection<E> implements TinkerOrderedSet<E> {

	protected OclStdLibOrderedSet<E> oclStdLibOrderedSet;

	@SuppressWarnings("unchecked")
	public TumlMemoryOrderedSet() {
		super();
		this.internalCollection = new ListOrderedSet();
		this.oclStdLibOrderedSet = new OclStdLibOrderedSetImpl<E>((ListOrderedSet) this.internalCollection);
		this.oclStdLibCollection = this.oclStdLibOrderedSet;
	}
	
	@SuppressWarnings("unchecked")
	public TumlMemoryOrderedSet(Collection<E> c) {
		super();
		this.internalCollection = ListOrderedSet.decorate(new ArrayList<E>(c));
		this.oclStdLibOrderedSet = new OclStdLibOrderedSetImpl<E>((ListOrderedSet) this.internalCollection);
		this.oclStdLibCollection = this.oclStdLibOrderedSet;
	}
	
	private ListOrderedSet getInternalListOrderedSet() {
		return (ListOrderedSet) this.internalCollection;
	}

	@Override
	public TinkerOrderedSet<E> select(BooleanExpressionEvaluator<E> e) {
		return this.oclStdLibOrderedSet.select(e);
	}

	@Override
	public <T, R> TinkerBag<T> collect(BodyExpressionEvaluator<R, E> e) {
		return this.oclStdLibOrderedSet.collect(e);
	}

	@Override
	public <R> TinkerBag<R> collectNested(BodyExpressionEvaluator<R, E> e) {
		return this.oclStdLibOrderedSet.collectNested(e);
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
	public TinkerOrderedSet<E> append(E e) {
		return this.oclStdLibOrderedSet.append(e);
	}

	@Override
	public TinkerOrderedSet<E> prepend(E e) {
		return this.oclStdLibOrderedSet.prepend(e);
	}

	@Override
	public TinkerOrderedSet<E> insertAt(Integer index, E e) {
		return this.oclStdLibOrderedSet.insertAt(index, e);
	}

	@Override
	public TinkerOrderedSet<E> subOrderedSet(Integer lower, Integer upper) {
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
	public TinkerOrderedSet<E> reverse() {
		return this.oclStdLibOrderedSet.reverse();
	}

	@Override
	public TinkerOrderedSet<E> including(E e) {
		return this.oclStdLibOrderedSet.including(e);
	}

}
