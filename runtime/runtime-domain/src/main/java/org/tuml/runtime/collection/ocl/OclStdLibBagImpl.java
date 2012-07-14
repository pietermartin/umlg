package org.tuml.runtime.collection.ocl;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.tuml.runtime.collection.TinkerBag;
import org.tuml.runtime.collection.TinkerCollection;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class OclStdLibBagImpl<E> extends OclStdLibCollectionImpl<E> implements TinkerBag<E> {

	private Multiset<E> bag;

	public OclStdLibBagImpl(Collection<E> bag) {
		this(HashMultiset.create(bag));
	}

	public OclStdLibBagImpl(Multiset<E> bag) {
		super(bag);
		this.bag = bag;
	}

	@Override
	public TinkerBag<E> select(BooleanExpressionWithV<E> v) {
		Multiset<E> result = HashMultiset.create();
		for (E e : this.collection) {
			if (v.evaluate(e)) {
				result.add(e);
			}
		}
		return new OclStdLibBagImpl<E>(result);
	}

	@Override
	public <R> TinkerBag<R> collectNested(BodyExpressionEvaluator<R, E> v) {
		Multiset<R> result = HashMultiset.create();
		for (E e : this.bag) {
			result.add(v.evaluate(e));
		}
		return new OclStdLibBagImpl<R>(result);
	}

	@Override
	public <T, R> TinkerBag<T> collect(BodyExpressionEvaluator<R, E> v) {
		return collectNested(v).<T> flatten();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> TinkerBag<R> flatten() {
		if (needsFlattening()) {
			Multiset<R> result = HashMultiset.create();
			for (E e : this.bag) {
				if (e instanceof TinkerCollection) {
					TinkerCollection<?> collection = (TinkerCollection<?>) e;
					result.addAll(collection.<R> flatten());
				} else {
					result.add((R) e);
				}
			}
			return new OclStdLibBagImpl<R>(result);
		} else {
			return new OclStdLibBagImpl<R>((Collection<R>) this.bag);
		}
	}
	
	private boolean needsFlattening() {
		return !this.bag.isEmpty() && this.bag.iterator().next() instanceof TinkerCollection;
	}

	@Override
	public int count(Object element) {
		return this.bag.count(element);
	}

	@Override
	public int add(E element, int occurrences) {
		return this.bag.add(element, occurrences);
	}

	@Override
	public int remove(Object element, int occurrences) {
		return this.bag.remove(element, occurrences);
	}

	@Override
	public int setCount(E element, int count) {
		return this.bag.setCount(element, count);
	}

	@Override
	public boolean setCount(E element, int oldCount, int newCount) {
		return this.bag.setCount(element, oldCount, newCount);
	}

	@Override
	public Set<E> elementSet() {
		return this.bag.elementSet();
	}

	@Override
	public Set<com.google.common.collect.Multiset.Entry<E>> entrySet() {
		return this.bag.entrySet();
	}

	@Override
	public Iterator<E> iterator() {
		return this.bag.iterator();
	}

	@Override
	public boolean contains(Object element) {
		return this.bag.contains(element);
	}

	@Override
	public boolean containsAll(Collection<?> elements) {
		return this.bag.containsAll(elements);
	}

	@Override
	public boolean add(E element) {
		return this.bag.add(element);
	}

	@Override
	public boolean remove(Object element) {
		return this.bag.remove(element);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.bag.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.bag.retainAll(c);
	}

	@Override
	public boolean isEmpty() {
		return this.bag.isEmpty();
	}

	@Override
	public Object[] toArray() {
		return this.bag.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.bag.toArray(a);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return this.bag.addAll(c);
	}

	@Override
	public void clear() {
		this.bag.clear();
	}

}
