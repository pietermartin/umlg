package org.tuml.runtime.collection.ocl;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.tuml.runtime.collection.TinkerBag;
import org.tuml.runtime.collection.TinkerCollection;
import org.tuml.runtime.collection.TinkerSet;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class OclStdLibBagImpl<E> extends OclStdLibCollectionImpl<E> implements TinkerBag<E> {

	private Multiset<E> bag;

//	public OclStdLibBagImpl(Collection<E> bag) {
//		this(HashMultiset.create(bag));
//	}
	
	/**
	 * A regular constructor compiles in eclipse but not in maven
	 * @param collection
	 * @return
	 */
	public static <E> OclStdLibBagImpl<E> get(Collection<E> bag) {
		return new OclStdLibBagImpl<E>(HashMultiset.create(bag));
	}

	public OclStdLibBagImpl(Multiset<E> bag) {
		super(bag);
		this.bag = bag;
	}

	@Override
	public Boolean equals(TinkerBag<E> bag) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public TinkerBag<E> union(TinkerBag<E> bag) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public TinkerBag<E> union(TinkerSet<E> set) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public TinkerBag<E> intersection(TinkerBag<E> bag) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public TinkerSet<E> intersection(TinkerSet<E> set) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public TinkerBag<E> including(E object) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public TinkerBag<E> excluding(E object) {
		throw new RuntimeException("Not implemented");
	}

	/***************************************************
	 * Iterate goodies
	 ***************************************************/

	@SuppressWarnings("unchecked")
	@Override
	public <T2> TinkerBag<T2> flatten() {
		if (needsFlattening()) {
			Multiset<T2> result = HashMultiset.create();
			for (E e : this.bag) {
				if (e instanceof TinkerCollection) {
					TinkerCollection<?> collection = (TinkerCollection<?>) e;
					result.addAll(collection.<T2> flatten());
				} else {
					result.add((T2) e);
				}
			}
			return new OclStdLibBagImpl<T2>(result);
		} else {
			return OclStdLibBagImpl.<T2>get((Collection<T2>) this.bag);
		}
	}
	
	/***************************************************
	 * Iterate goodies
	 ***************************************************/
	
	@Override
	public TinkerBag<E> select(BooleanExpressionEvaluator<E> v) {
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
			R evaluate = v.evaluate(e);
			if (evaluate != null) {
				result.add(evaluate);
			}
		}
		return new OclStdLibBagImpl<R>(result);
	}

	@Override
	public <T, R> TinkerBag<T> collect(BodyExpressionEvaluator<R, E> v) {
		return collectNested(v).<T> flatten();
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

	@Override
	public String toJson() {
		throw new RuntimeException("Not yet implemented");
	}
}
