package org.tuml.runtime.collection.impl;

import java.util.Set;

import org.tuml.runtime.collection.TinkerBag;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.collection.ocl.BodyExpressionEvaluator;
import org.tuml.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.tuml.runtime.collection.ocl.OclStdLibBag;
import org.tuml.runtime.collection.ocl.OclStdLibBagImpl;
import org.tuml.runtime.domain.TumlNode;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public abstract class BaseBag<E> extends BaseCollection<E> implements TinkerBag<E> {

	protected OclStdLibBag<E> oclStdLibBag;
	
	public BaseBag(TumlNode owner, TumlRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
		this.internalCollection = HashMultiset.create();
		this.oclStdLibBag = new OclStdLibBagImpl<E>((Multiset<E>)this.internalCollection); 
		this.oclStdLibCollection = this.oclStdLibBag;
	}
	
	protected Multiset<E> getInternalBag() {
		return (Multiset<E>) this.internalCollection;
	}
	
	@Override
	public int count(Object element) {
		maybeLoad();
		return this.getInternalBag().count(element);
	}

	@Override
	public int add(E element, int occurrences) {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public int remove(Object element, int occurrences) {
		maybeLoad();
		int count = count(element);
		if (count > occurrences) {
			for (int i = 0; i < occurrences; i++) {
				remove(element);
			}
		} else {
			for (int i = 0; i < count; i++) {
				remove(element);
			}
		}
		return count;
	}
	
	@Override
	public void clear() {
		maybeLoad();
		Multiset<E> tmp = HashMultiset.create();
		tmp.addAll(this.getInternalBag());
		for (E e : tmp) {
			this.remove(e);
		}
	}

	@Override
	public int setCount(E element, int count) {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean setCount(E element, int oldCount, int newCount) {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Set<E> elementSet() {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Set<com.google.common.collect.Multiset.Entry<E>> entrySet() {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public <R> TinkerBag<R> collectNested(BodyExpressionEvaluator<R, E> v) {
		maybeLoad();
		return this.oclStdLibBag.collectNested(v);
	}

	@Override
	public <T, R> TinkerBag<T> collect(BodyExpressionEvaluator<R, E> v) {
		maybeLoad();
		return this.oclStdLibBag.collect(v);
	}

	@Override
	public <R> TinkerBag<R> flatten() {
		maybeLoad();
		return this.oclStdLibBag.flatten();
	}
	
	@Override
	public TinkerBag<E> select(BooleanExpressionEvaluator<E> v) {
		maybeLoad();
		return this.oclStdLibBag.select(v);
	}

	@Override
	public Boolean equals(TinkerBag<E> bag) {
		// TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public TinkerBag<E> union(TinkerBag<E> bag) {
		// TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public TinkerBag<E> union(TinkerSet<E> set) {
		// TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public TinkerBag<E> intersection(TinkerBag<E> bag) {
		// TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public TinkerSet<E> intersection(TinkerSet<E> set) {
		// TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public TinkerBag<E> including(E object) {
		// TODO Implement
		throw new RuntimeException("Not implemented");
	}

	@Override
	public TinkerBag<E> excluding(E object) {
		// TODO Implement
		throw new RuntimeException("Not implemented");
	}

	
}
