package org.tuml.runtime.util;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.tuml.runtime.collection.TinkerBag;
import org.tuml.runtime.collection.TinkerCollection;
import org.tuml.runtime.collection.TinkerOrderedSet;
import org.tuml.runtime.collection.TinkerSequence;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.ocl.BodyExpressionEvaluator;
import org.tuml.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.tuml.runtime.collection.ocl.IterateExpressionAccumulator;
import org.tuml.runtime.domain.TumlNode;
import org.tuml.runtime.domain.ocl.OclAny;
import org.tuml.runtime.domain.ocl.OclState;

public class TumlCollections {

	public static final TinkerSet<Object> EMPTY_TUML_SET = new EmptySet<Object>();
	
    @SuppressWarnings("unchecked")
    public static final <T> TinkerSet<T> emptySet() {
        return (TinkerSet<T>) EMPTY_TUML_SET;
    }
	
    /**
     * Copied from jdk
     * @serial include
     */
    private static class EmptySet<E>
        extends AbstractSet<E>
        implements Serializable, TinkerSet<E>
    {
        private static final long serialVersionUID = 1582296315990362920L;

        public Iterator<E> iterator() { return emptyIterator(); }

        public int size() {return 0;}
        public boolean isEmpty() {return true;}

        public boolean contains(Object obj) {return false;}
        public boolean containsAll(Collection<?> c) { return c.isEmpty(); }

        public Object[] toArray() { return new Object[0]; }

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
	        } catch (ClassCastException unused)   {
	            return false;
	        } catch (NullPointerException unused) {
	            return false;
	        }
	    }

		@Override
		public <R> R iterate(IterateExpressionAccumulator<R, E> v) {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public E any(BooleanExpressionEvaluator<E> e) {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public TinkerSet<E> asSet() {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public TinkerOrderedSet<E> asOrderedSet() {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public TinkerSequence<E> asSequence() {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public TinkerBag<E> asBag() {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public TinkerSet<E> select(BooleanExpressionEvaluator<E> e) {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public <R> TinkerBag<R> collectNested(BodyExpressionEvaluator<R, E> e) {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public <T, R> TinkerBag<T> collect(BodyExpressionEvaluator<R, E> e) {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public <R> TinkerSet<R> flatten() {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public boolean notEquals() {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public boolean includes(E t) {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public boolean excludes(E t) {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public int count(E e) {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public Boolean includesAll(TinkerCollection<E> c) {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public Boolean excludesAll(TinkerCollection<E> c) {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public Boolean notEmpty() {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public E max() {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public E min() {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public E sum() {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public TinkerSet<?> product(TinkerCollection<E> c) {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public Boolean equals(OclAny oclAny) {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public Boolean notEquals(OclAny oclAny) {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public Boolean oclIsNew() {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public Boolean oclIsUndefined() {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public Boolean oclIsInvalid() {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public <T> T oclAsType(T classifier) {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public Boolean oclIsTypeOf(TumlNode classifier) {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public Boolean oclIsKindOf(TumlNode classifier) {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public Boolean oclIsInState(OclState state) {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public TumlNode oclType() {
			//TODO Implement
			throw new RuntimeException("Not implemented");
		}

		@Override
		public String oclLocale() {
			//TODO Implement
			throw new RuntimeException("Not implemented");
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
    }
	
    @SuppressWarnings("unchecked")
    private static <T> Iterator<T> emptyIterator() {
        return (Iterator<T>) EmptyIterator.EMPTY_ITERATOR;
    }
    
    private static class EmptyIterator<E> implements Iterator<E> {
        static final EmptyIterator<Object> EMPTY_ITERATOR
            = new EmptyIterator<Object>();

        public boolean hasNext() { return false; }
        public E next() { throw new NoSuchElementException(); }
        public void remove() { throw new IllegalStateException(); }
    }

}
