package org.umlg.runtime.collection.memory;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.umlg.runtime.collection.UmlgBag;
import org.umlg.runtime.collection.UmlgSequence;
import org.umlg.runtime.collection.UmlgSet;
import org.umlg.runtime.collection.ocl.BodyExpressionEvaluator;
import org.umlg.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.umlg.runtime.collection.ocl.OclStdLibBag;
import org.umlg.runtime.collection.ocl.OclStdLibBagImpl;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

public class UmlgMemoryBag<E> extends UmlgMemoryCollection<E> implements UmlgBag<E> {

	protected OclStdLibBag<E> oclStdLibBag;

	public UmlgMemoryBag() {
		super();
		this.internalCollection = HashMultiset.create();
		this.oclStdLibBag = new OclStdLibBagImpl<E>((Multiset<E>) this.internalCollection);
		this.oclStdLibCollection = this.oclStdLibBag;
	}

	public UmlgMemoryBag(Collection<E> c) {
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
	public int count(Object element){
		return oclStdLibBag.count((E)element);
	}
	
	@Override
	public <T2> UmlgBag<T2> flatten() {
		return this.oclStdLibBag.flatten();
	}
	
	@Override
	public <T, R> UmlgBag<T> collect(BodyExpressionEvaluator<R, E> e) {
		return this.oclStdLibBag.collect(e);
	}

	@Override
	public <R> UmlgBag<R> collectNested(BodyExpressionEvaluator<R, E> e) {
		return this.oclStdLibBag.collectNested(e);
	}

	@Override
	public UmlgBag<E> select(BooleanExpressionEvaluator<E> e) {
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
	public Boolean equals(UmlgBag<E> bag) {
		return this.oclStdLibBag.equals(bag);
	}

	@Override
	public UmlgBag<E> union(UmlgBag<E> bag) {
		return this.oclStdLibBag.union(bag);
	}

	@Override
	public UmlgBag<E> union(UmlgSet<E> set) {
		return this.oclStdLibBag.union(set);
	}

	@Override
	public UmlgBag<E> intersection(UmlgBag<E> bag) {
		return this.oclStdLibBag.intersection(bag);
	}

	@Override
	public UmlgSet<E> intersection(UmlgSet<E> set) {
		return this.oclStdLibBag.intersection(set);
	}

	@Override
	public UmlgBag<E> including(E object) {
		return this.oclStdLibBag.including(object);
	}

	@Override
	public UmlgBag<E> excluding(E object) {
		return this.oclStdLibBag.excluding(object);
	}

	@Override
	public UmlgSequence<E> sortedBy(Comparator<E> comparator) {
		return this.oclStdLibBag.sortedBy(comparator);
	}
}
