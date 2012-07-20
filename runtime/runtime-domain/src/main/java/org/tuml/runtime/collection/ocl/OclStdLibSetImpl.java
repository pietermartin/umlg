package org.tuml.runtime.collection.ocl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.tuml.runtime.collection.TinkerBag;
import org.tuml.runtime.collection.TinkerCollection;
import org.tuml.runtime.collection.TinkerSet;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class OclStdLibSetImpl<E> extends OclStdLibCollectionImpl<E> implements TinkerSet<E> {

	private Set<E> set;

	public OclStdLibSetImpl(Collection<E> collection) {
		this(new HashSet<E>(collection));
	}

	public OclStdLibSetImpl(Set<E> set) {
		super(set);
		this.set = set;
	}
	
	@Override
	public TinkerSet<E> union(TinkerSet<E> s) {
		//TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public TinkerBag<E> union(TinkerBag<E> bag) {
		//TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Boolean equals(TinkerSet<E> s) {
		//TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public TinkerSet<E> intersection(TinkerSet<E> s) {
		//TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public TinkerSet<E> intersection(TinkerBag<E> bag) {
		//TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public TinkerSet<E> subtract(TinkerSet<E> s) {
		//TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public TinkerSet<E> including(E e) {
		//TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public TinkerSet<E> excluding(E e) {
		//TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public TinkerSet<E> symmetricDifference(TinkerSet<E> s) {
		//TODO Implement
		throw new RuntimeException("Not implemented");
	}	

	
	/***************************************************
	 * Iterate goodies
	 ***************************************************/
	
	@Override
	public TinkerSet<E> select(BooleanExpressionEvaluator<E> v) {
		Set<E> result = new HashSet<E>();
		for (E e : this.collection) {
			if (v.evaluate(e)) {
				result.add(e);
			}
		}
		return new OclStdLibSetImpl<E>(result);
	}
	
	@Override
	public <R> TinkerBag<R> collectNested(BodyExpressionEvaluator<R, E> v) {
		Multiset<R> result = HashMultiset.create();
		for (E e : this.set) {
			result.add(v.evaluate(e));
		}
		return new OclStdLibBagImpl<R>(result);
	}
	
	@Override
	public <T, R> TinkerBag<T> collect(BodyExpressionEvaluator<R, E> v) {
		return collectNested(v).flatten();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> TinkerSet<R> flatten() {
		Set<R> result = new HashSet<R>();
		for (E e : this.set) {
			if (e instanceof TinkerCollection) {
				TinkerCollection<?> collection = (TinkerCollection<?>)e;
				result.addAll(collection.<R>flatten());
			} else {
				result.add((R)e);
			}
		}
		return new OclStdLibSetImpl<R>(result);
	}

	@Override
	public boolean isEmpty() {
		return this.set.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return this.set.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return this.set.iterator();
	}

	@Override
	public Object[] toArray() {
		return this.set.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.set.toArray(a);
	}

	@Override
	public boolean add(E e) {
		return this.set.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return this.set.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return this.set.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return this.set.addAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.set.retainAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.set.removeAll(c);
	}

	@Override
	public void clear() {
		this.set.clear();
	}
	
}
