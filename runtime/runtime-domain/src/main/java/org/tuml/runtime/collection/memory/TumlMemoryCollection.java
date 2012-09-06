package org.tuml.runtime.collection.memory;

import java.util.Collection;
import java.util.Iterator;

import org.tuml.runtime.collection.TinkerBag;
import org.tuml.runtime.collection.TinkerCollection;
import org.tuml.runtime.collection.TinkerOrderedSet;
import org.tuml.runtime.collection.TinkerSequence;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.ocl.BodyExpressionEvaluator;
import org.tuml.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.tuml.runtime.collection.ocl.IterateExpressionAccumulator;
import org.tuml.runtime.collection.ocl.OclStdLibCollection;
import org.tuml.runtime.domain.ocl.OclState;

public class TumlMemoryCollection<E> implements TinkerCollection<E> {

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
	public boolean equals(TinkerCollection<E> c) {
		return this.oclStdLibCollection.equals(c);
	}

	@Override
	public boolean notEquals(TinkerCollection<E> c) {
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
	public Boolean includesAll(TinkerCollection<E> c) {
		return this.oclStdLibCollection.includesAll(c);
	}

	@Override
	public Boolean excludesAll(TinkerCollection<E> c) {
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
	public TinkerSet<?> product(TinkerCollection<E> c) {
		return this.oclStdLibCollection.product(c);
	}

	@Override
	public TinkerSet<E> asSet() {
		return this.oclStdLibCollection.asSet();
	}

	@Override
	public TinkerOrderedSet<E> asOrderedSet() {
		return this.oclStdLibCollection.asOrderedSet();
	}

	@Override
	public TinkerSequence<E> asSequence() {
		return this.oclStdLibCollection.asSequence();
	}

	@Override
	public TinkerBag<E> asBag() {
		return this.oclStdLibCollection.asBag();
	}

	@Override
	public <T2> TinkerCollection<T2> flatten() {
		return this.oclStdLibCollection.flatten();
	}

	@Override
	public <R> R iterate(IterateExpressionAccumulator<R, E> v) {
		return this.oclStdLibCollection.iterate(v);
	}

	@Override
	public TinkerCollection<E> select(BooleanExpressionEvaluator<E> e) {
		return this.oclStdLibCollection.select(e);
	}

	@Override
	public E any(BooleanExpressionEvaluator<E> e) {
		return this.oclStdLibCollection.any(e);
	}

	@Override
	public <T, R> TinkerCollection<T> collect(BodyExpressionEvaluator<R, E> e) {
		return this.oclStdLibCollection.collect(e);
	}

	@Override
	public <R> TinkerCollection<R> collectNested(BodyExpressionEvaluator<R, E> e) {
		return this.oclStdLibCollection.collectNested(e);
	}

	@Override
	public String toJson() {
		//TODO
		throw new RuntimeException("Not yet implemented");
	}
}
