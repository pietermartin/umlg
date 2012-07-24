package org.tuml.runtime.collection.memory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import org.tuml.runtime.collection.TinkerSequence;
import org.tuml.runtime.collection.ocl.BodyExpressionEvaluator;
import org.tuml.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.tuml.runtime.collection.ocl.OclStdLibSequence;
import org.tuml.runtime.collection.ocl.OclStdLibSequenceImpl;

public class TumlMemorySequence<E> extends TumlMemoryCollection<E> implements TinkerSequence<E> {

	protected OclStdLibSequence<E> oclStdLibSequence;

	public TumlMemorySequence() {
		super();
		this.internalCollection = new ArrayList<E>();
		this.oclStdLibSequence = new OclStdLibSequenceImpl<E>((List<E>) this.internalCollection);
		this.oclStdLibCollection = this.oclStdLibSequence;
	}

	public TumlMemorySequence(Collection<E> c) {
		super();
		this.internalCollection = new ArrayList<E>(c);
		this.oclStdLibSequence = new OclStdLibSequenceImpl<E>((List<E>) this.internalCollection);
		this.oclStdLibCollection = this.oclStdLibSequence;
	}

	protected List<E> getInternalList() {
		return (List<E>) this.internalCollection;
	}

	@Override
	public <T2> TinkerSequence<T2> flatten() {
		return this.oclStdLibSequence.flatten();
	}

	@Override
	public <T, R> TinkerSequence<T> collect(BodyExpressionEvaluator<R, E> e) {
		return this.oclStdLibSequence.collect(e);
	}

	@Override
	public <R> TinkerSequence<R> collectNested(BodyExpressionEvaluator<R, E> e) {
		return this.oclStdLibSequence.collectNested(e);
	}

	@Override
	public TinkerSequence<E> select(BooleanExpressionEvaluator<E> e) {
		return this.oclStdLibSequence.select(e);
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		return getInternalList().addAll(index, c);
	}

	@Override
	public E get(int index) {
		return getInternalList().get(index);
	}

	@Override
	public E set(int index, E element) {
		return getInternalList().set(index, element);
	}

	@Override
	public void add(int index, E element) {
		getInternalList().add(index, element);
	}

	@Override
	public E remove(int index) {
		return getInternalList().remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return getInternalList().indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return getInternalList().lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		return getInternalList().listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return getInternalList().listIterator(index);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return getInternalList().subList(fromIndex, toIndex);
	}

	@Override
	public Boolean equals(TinkerSequence<E> s) {
		return this.oclStdLibSequence.equals(s);
	}

	@Override
	public TinkerSequence<E> union(TinkerSequence<? extends E> s) {
		return this.oclStdLibSequence.union(s);
	}

	@Override
	public TinkerSequence<E> append(E object) {
		return this.oclStdLibSequence.append(object);
	}

	@Override
	public TinkerSequence<E> prepend(E object) {
		return this.oclStdLibSequence.prepend(object);
	}

	@Override
	public TinkerSequence<E> insertAt(Integer index, E object) {
		return this.oclStdLibSequence.insertAt(index, object);
	}

	@Override
	public TinkerSequence<E> subSequence(Integer lower, Integer upper) {
		return this.oclStdLibSequence.subSequence(lower, upper);
	}

	@Override
	public E at(Integer i) {
		return this.oclStdLibSequence.at(i);
	}

	@Override
	public E first() {
		return this.oclStdLibSequence.first();
	}

	@Override
	public E last() {
		return this.oclStdLibSequence.last();
	}

	@Override
	public TinkerSequence<E> including(E object) {
		return this.oclStdLibSequence.including(object);
	}

	@Override
	public TinkerSequence<E> excluding(E object) {
		return this.oclStdLibSequence.excluding(object);
	}

	@Override
	public TinkerSequence<E> reverse() {
		return this.oclStdLibSequence.reverse();
	}

}
