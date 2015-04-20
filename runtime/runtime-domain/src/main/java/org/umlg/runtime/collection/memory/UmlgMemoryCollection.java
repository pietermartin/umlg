package org.umlg.runtime.collection.memory;

import org.umlg.runtime.collection.*;
import org.umlg.runtime.collection.ocl.BodyExpressionEvaluator;
import org.umlg.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.umlg.runtime.collection.ocl.IterateExpressionAccumulator;
import org.umlg.runtime.collection.ocl.OclStdLibCollection;
import org.umlg.runtime.domain.ocl.OclState;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public class UmlgMemoryCollection<E> implements UmlgCollection<E> {

	protected Collection<E> internalCollection;
	protected OclStdLibCollection<E> oclStdLibCollection;

	@Override
	public int size() {
		return this.internalCollection.size();
	}

	@Override
	public boolean isEmpty() {
		return this.internalCollection.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return this.internalCollection.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return this.internalCollection.iterator();
	}

	@Override
	public Object[] toArray() {
		return this.internalCollection.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.internalCollection.toArray(a);
	}

    @Override
    public boolean inverseAdder(E e) {
        return this.internalCollection.add(e);
    }

    @Override
	public boolean add(E e) {
		return this.internalCollection.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return this.internalCollection.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return this.internalCollection.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return this.internalCollection.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.internalCollection.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.internalCollection.retainAll(c);
	}

	@Override
	public void clear() {
		this.internalCollection.clear();
	}

	@Override
	public boolean notEquals(Object object) {
		return this.oclStdLibCollection.notEquals(object);
	}

	@Override
	public Boolean oclIsNew() {
		return this.oclStdLibCollection.oclIsNew();
	}

	@Override
	public Boolean oclIsUndefined() {
		return this.oclStdLibCollection.oclIsUndefined();
	}

	@Override
	public Boolean oclIsInvalid() {
		return this.oclStdLibCollection.oclIsInvalid();
	}

	@Override
	public <T> T oclAsType(T classifier) {
		return this.oclStdLibCollection.oclAsType(classifier);
	}

	@Override
	public Boolean oclIsTypeOf(Object object) {
		return this.oclStdLibCollection.oclIsTypeOf(object);
	}

	@Override
	public Boolean oclIsKindOf(Object object) {
		return this.oclStdLibCollection.oclIsKindOf(object);
	}

	@Override
	public Boolean oclIsInState(OclState state) {
		return this.oclStdLibCollection.oclIsInState(state);
	}

	@Override
	public <T> Class<T> oclType() {
		return this.oclStdLibCollection.oclType();
	}

	@Override
	public String oclLocale() {
		return this.oclStdLibCollection.oclLocale();
	}

	@Override
	public boolean equals(UmlgCollection<E> c) {
		return this.oclStdLibCollection.equals(c);
	}

	@Override
	public boolean notEquals(UmlgCollection<E> c) {
		return this.oclStdLibCollection.notEquals(c);
	}

	@Override
	public boolean includes(E t) {
		return this.oclStdLibCollection.includes(t);
	}

	@Override
	public boolean excludes(E t) {
		return this.oclStdLibCollection.excludes(t);
	}

	@Override
	public int count(E e) {
		return this.oclStdLibCollection.count(e);
	}

	@Override
	public Boolean includesAll(UmlgCollection<E> c) {
		return this.oclStdLibCollection.includesAll(c);
	}

	@Override
	public Boolean excludesAll(UmlgCollection<E> c) {
		return this.oclStdLibCollection.excludesAll(c);
	}

	@Override
	public Boolean notEmpty() {
		return this.oclStdLibCollection.notEmpty();
	}

	@Override
	public E max() {
		return this.oclStdLibCollection.max();
	}

	@Override
	public E min() {
		return this.oclStdLibCollection.min();
	}

	@Override
	public E sum() {
		return this.oclStdLibCollection.sum();
	}

	@Override
	public UmlgSet<?> product(UmlgCollection<E> c) {
		return this.oclStdLibCollection.product(c);
	}

	@Override
	public UmlgSet<E> asSet() {
		return this.oclStdLibCollection.asSet();
	}

	@Override
	public UmlgOrderedSet<E> asOrderedSet() {
		return this.oclStdLibCollection.asOrderedSet();
	}

	@Override
	public UmlgSequence<E> asSequence() {
		return this.oclStdLibCollection.asSequence();
	}

	@Override
	public UmlgBag<E> asBag() {
		return this.oclStdLibCollection.asBag();
	}

	@Override
	public <T2> UmlgCollection<T2> flatten() {
		return this.oclStdLibCollection.flatten();
	}

	@Override
	public <R> R iterate(IterateExpressionAccumulator<R, E> v) {
		return this.oclStdLibCollection.iterate(v);
	}

	@Override
	public UmlgCollection<E> select(BooleanExpressionEvaluator<E> e) {
		return this.oclStdLibCollection.select(e);
	}

	@Override
	public E any(BooleanExpressionEvaluator<E> e) {
		return this.oclStdLibCollection.any(e);
	}

	@Override
	public <T, R> UmlgCollection<T> collect(BodyExpressionEvaluator<R, E> e) {
		return this.oclStdLibCollection.collect(e);
	}

	@Override
	public <R> UmlgCollection<R> collectNested(BodyExpressionEvaluator<R, E> e) {
		return this.oclStdLibCollection.collectNested(e);
	}

    @Override
    public <R> Boolean isUnique(BodyExpressionEvaluator<R, E> e) {
        return this.oclStdLibCollection.isUnique(e);
    }

    @Override
    public Boolean exists(BooleanExpressionEvaluator<E> e) {
        return this.oclStdLibCollection.exists(e);
    }

    @Override
    public Boolean forAll(BooleanExpressionEvaluator<E> e) {
        return this.oclStdLibCollection.forAll(e);
    }

    @Override
	public String toJson() {
		//TODO
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public UmlgCollection<E> sortedBy(Comparator<E> comparator) {
		return this.oclStdLibCollection.sortedBy(comparator);
	}
}
