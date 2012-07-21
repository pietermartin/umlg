package org.tuml.runtime.util;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.TreeSet;

import org.apache.commons.collections.set.ListOrderedSet;
import org.tuml.runtime.collection.TinkerBag;
import org.tuml.runtime.collection.TinkerCollection;
import org.tuml.runtime.collection.TinkerOrderedSet;
import org.tuml.runtime.collection.TinkerSequence;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.ocl.BodyExpressionEvaluator;
import org.tuml.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.tuml.runtime.collection.ocl.IterateExpressionAccumulator;
import org.tuml.runtime.domain.ocl.OclState;

public class TumlCollections {

	@SuppressWarnings("rawtypes")
	public static final TinkerSet EMPTY_TUML_SET = new EmptySet<Object>();

	@SuppressWarnings("unchecked")
	public static final TinkerSet<Object> emptySet() {
		return EMPTY_TUML_SET;
	}

	/**
	 * Copied from jdk
	 * 
	 * @serial include
	 */
	private static class EmptySet<E> extends AbstractSet<E> implements Serializable, TinkerSet<E> {

		private static final long serialVersionUID = -4525859847873655693L;

		public Iterator<E> iterator() {
			return emptyIterator();
		}

		public int size() {
			return 0;
		}

		public boolean isEmpty() {
			return true;
		}

		public boolean contains(Object obj) {
			return false;
		}

		public boolean containsAll(Collection<?> c) {
			return c.isEmpty();
		}

		public Object[] toArray() {
			return new Object[0];
		}

		public <T> T[] toArray(T[] a) {
			if (a.length > 0)
				a[0] = null;
			return a;
		}

		// Preserves singleton property
		private Object readResolve() {
			return EMPTY_TUML_SET;
		}

		@Override
		public boolean equals(TinkerCollection<E> o) {
			if (o == this)
				return true;

			if (!(o instanceof TinkerSet))
				return false;
			@SuppressWarnings("rawtypes")
			Collection c = (Collection) o;
			if (c.size() != size())
				return false;
			try {
				return containsAll(c);
			} catch (ClassCastException unused) {
				return false;
			} catch (NullPointerException unused) {
				return false;
			}
		}

		@Override
		public boolean notEquals(TinkerCollection<E> c) {
			return !equals(c);
		}
		
		@Override
		public boolean equals(Object object) {
			// TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public boolean notEquals(Object object) {
			// TODO Implement
			throw new RuntimeException("Not implemented");
		}


		@Override
		public <R> R iterate(IterateExpressionAccumulator<R, E> v) {
			return null;
		}

		@Override
		public E any(BooleanExpressionEvaluator<E> e) {
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public TinkerSet<E> asSet() {
			return EMPTY_TUML_SET;
		}

		@Override
		public TinkerOrderedSet<E> asOrderedSet() {
			// TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public TinkerSequence<E> asSequence() {
			// TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public TinkerBag<E> asBag() {
			// TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public TinkerSet<E> select(BooleanExpressionEvaluator<E> e) {
			return EMPTY_TUML_SET;
		}

		@Override
		public <R> TinkerBag<R> collectNested(BodyExpressionEvaluator<R, E> e) {
			// TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public <T, R> TinkerBag<T> collect(BodyExpressionEvaluator<R, E> e) {
			// TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public <R> TinkerSet<R> flatten() {
			return EMPTY_TUML_SET;
		}

		@Override
		public boolean includes(E t) {
			return false;
		}

		@Override
		public boolean excludes(E t) {
			return true;
		}

		@Override
		public int count(E e) {
			return 0;
		}

		@Override
		public Boolean includesAll(TinkerCollection<E> c) {
			return c.isEmpty();
		}

		@Override
		public Boolean excludesAll(TinkerCollection<E> c) {
			return !c.isEmpty();
		}

		@Override
		public Boolean notEmpty() {
			return false;
		}

		@Override
		public E max() {
			return null;
		}

		@Override
		public E min() {
			return null;
		}

		@Override
		public E sum() {
			return null;
		}

		@Override
		public TinkerSet<E> product(TinkerCollection<E> c) {
			return EMPTY_TUML_SET;
		}

		@Override
		public Boolean oclIsNew() {
			// TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public Boolean oclIsUndefined() {
			// TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public Boolean oclIsInvalid() {
			// TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public <T> T oclAsType(T classifier) {
			// TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public Boolean oclIsTypeOf(Object object) {
			// TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public Boolean oclIsKindOf(Object object) {
			// TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public Boolean oclIsInState(OclState state) {
			// TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public <T  extends Object> Class<T> oclType() {
			// TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public String oclLocale() {
			// TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public TinkerSet<E> union(TinkerSet<E> s) {
			// TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public TinkerBag<E> union(TinkerBag<E> bag) {
			// TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public Boolean equals(TinkerSet<E> s) {
			// TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public TinkerSet<E> intersection(TinkerSet<E> s) {
			// TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public TinkerSet<E> intersection(TinkerBag<E> bag) {
			// TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public TinkerSet<E> subtract(TinkerSet<E> s) {
			// TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public TinkerSet<E> including(E e) {
			// TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public TinkerSet<E> excluding(E e) {
			// TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public TinkerSet<E> symmetricDifference(TinkerSet<E> s) {
			// TODO Implement
			throw new RuntimeException("Not implemented");
		}

	}

	@SuppressWarnings("unchecked")
	private static <T> Iterator<T> emptyIterator() {
		return (Iterator<T>) EmptyIterator.EMPTY_ITERATOR;
	}

	private static class EmptyIterator<E> implements Iterator<E> {
		static final EmptyIterator<Object> EMPTY_ITERATOR = new EmptyIterator<Object>();

		public boolean hasNext() {
			return false;
		}

		public E next() {
			throw new NoSuchElementException();
		}

		public void remove() {
			throw new IllegalStateException();
		}
	}

}
