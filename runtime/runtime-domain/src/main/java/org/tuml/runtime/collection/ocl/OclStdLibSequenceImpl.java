package org.tuml.runtime.collection.ocl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.tuml.runtime.collection.TinkerSequence;

public class OclStdLibSequenceImpl<E> extends OclStdLibCollectionImpl<E> implements TinkerSequence<E> {

	private List<E> list;

	public OclStdLibSequenceImpl(Collection<E> collection) {
		this(new ArrayList<E>(collection));
	}

	public OclStdLibSequenceImpl(List<E> list) {
		super(list);
		this.list = list;
	}

	@Override
	public Boolean equals(TinkerSequence<E> s) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public TinkerSequence<E> union(TinkerSequence<E> s) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public TinkerSequence<E> append(E object) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public TinkerSequence<E> prepend(E object) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public TinkerSequence<E> insertAt(Integer index, E object) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public TinkerSequence<E> subSequence(Integer lower, Integer upper) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public E at(Integer i) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public int indexOf(Object obj) {
		throw new RuntimeException("Not implemented");
	}
	
//	@Override
//	public int indexOf(Object o) {
//		return this.list.indexOf(o);
//	}


	@Override
	public E last() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public TinkerSequence<E> including(E e) {
		if (e != null) {
			this.list.add(e);
		}
		return this;
	}

	@Override
	public TinkerSequence<E> excluding(E object) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public TinkerSequence<E> reverse() {
		throw new RuntimeException("Not implemented");
	}	

	/***************************************************
	 * Iterate goodies
	 ***************************************************/
	
	@Override
	public TinkerSequence<E> select(BooleanExpressionEvaluator<E> v) {
		List<E> result = new ArrayList<E>();
		for (E e : this.collection) {
			if (v.evaluate(e)) {
				result.add(e);
			}
		}
		return new OclStdLibSequenceImpl<E>(result);
	}

	@Override
	public <R> TinkerSequence<R> collectNested(BodyExpressionEvaluator<R, E> v) {
		List<R> result = new ArrayList<R>();
		for (E e : this.list) {
			result.add(v.evaluate(e));
		}
		return new OclStdLibSequenceImpl<R>(result);
	}
	
	@Override
	public <T, R> TinkerSequence<T> collect(BodyExpressionEvaluator<R, E> v) {
		return collectNested(v).flatten();
	}

	@Override
	public <R> TinkerSequence<R> flatten() {
		return null;
	}

	@Override
	public boolean isEmpty() {
		return this.list.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return this.list.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return this.list.iterator();
	}

	@Override
	public Object[] toArray() {
		return this.list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.list.toArray(a);
	}

	@Override
	public boolean add(E e) {
		return this.list.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return this.list.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return this.list.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return this.list.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		return this.list.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.list.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.list.retainAll(c);
	}

	@Override
	public void clear() {
		this.list.clear();
	}

	@Override
	public E get(int index) {
		return this.list.get(index);
	}

	@Override
	public E set(int index, E element) {
		return this.list.set(index, element);
	}

	@Override
	public void add(int index, E element) {
		this.list.add(index, element);
	}

	@Override
	public E remove(int index) {
		return this.list.remove(index);
	}

	@Override
	public int lastIndexOf(Object o) {
		return this.list.lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		return this.list.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return this.list.listIterator(index);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return this.list.subList(fromIndex, toIndex);
	}

	@Override
	public E first() {
		return this.list.get(0);
	}
	
}
