package org.tuml.runtime.collection.memory;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.tuml.runtime.collection.TinkerBag;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.ocl.BodyExpressionEvaluator;
import org.tuml.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.tuml.runtime.collection.ocl.OclStdLibBag;
import org.tuml.runtime.collection.ocl.OclStdLibBagImpl;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;

public class TumlMemoryBag <E> extends TumlMemoryCollection<E> implements TinkerBag<E> {

	protected OclStdLibBag<E> oclStdLibBag;

	public TumlMemoryBag() {
		super();
		this.internalCollection = HashMultiset.create();
		this.oclStdLibBag = new OclStdLibBagImpl<E>((Multiset<E>) this.internalCollection);
		this.oclStdLibCollection = this.oclStdLibBag;
	}

	public TumlMemoryBag(Collection<E> c) {
		super();
		this.internalCollection = HashMultiset.create(c);
		this.oclStdLibBag = new OclStdLibBagImpl<E>((Multiset<E>) this.internalCollection);
		this.oclStdLibCollection = this.oclStdLibBag;
	}
	
	private Multiset<E> getInternalBag() {
		return (Multiset<E>) this.internalCollection;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int count(@Nullable Object element){
		return oclStdLibBag.count((E)element);
	}
	
	@Override
	public <T2> TinkerBag<T2> flatten() {
		return this.oclStdLibBag.flatten();
	}
	
	@Override
	public <T, R> TinkerBag<T> collect(BodyExpressionEvaluator<R, E> e) {
		return this.oclStdLibBag.collect(e);
	}

	@Override
	public <R> TinkerBag<R> collectNested(BodyExpressionEvaluator<R, E> e) {
		return this.oclStdLibBag.collectNested(e);
	}

	@Override
	public TinkerBag<E> select(BooleanExpressionEvaluator<E> e) {
		return this.oclStdLibBag.select(e);
	}

	@Override
	public int add(E element, int occurrences) {
		return getInternalBag().add(element, occurrences);
	}

	@Override
	public int remove(Object element, int occurrences) {
		return getInternalBag().remove(element, occurrences);
	}

	@Override
	public int setCount(E element, int count) {
		return getInternalBag().setCount(element, count);
	}

	@Override
	public boolean setCount(E element, int oldCount, int newCount) {
		return getInternalBag().setCount(element, oldCount, newCount);
	}

	@Override
	public Set<E> elementSet() {
		return getInternalBag().elementSet();
	}

	@Override
	public Set<com.google.common.collect.Multiset.Entry<E>> entrySet() {
		return getInternalBag().entrySet();
	}

	@Override
	public Boolean equals(TinkerBag<E> bag) {
		return this.oclStdLibBag.equals(bag);
	}

	@Override
	public TinkerBag<E> union(TinkerBag<E> bag) {
		return this.oclStdLibBag.union(bag);
	}

	@Override
	public TinkerBag<E> union(TinkerSet<E> set) {
		return this.oclStdLibBag.union(set);
	}

	@Override
	public TinkerBag<E> intersection(TinkerBag<E> bag) {
		return this.oclStdLibBag.intersection(bag);
	}

	@Override
	public TinkerSet<E> intersection(TinkerSet<E> set) {
		return this.oclStdLibBag.intersection(set);
	}

	@Override
	public TinkerBag<E> including(E object) {
		return this.oclStdLibBag.including(object);
	}

	@Override
	public TinkerBag<E> excluding(E object) {
		return this.oclStdLibBag.excluding(object);
	}
	
}
