package org.tuml.runtime.util;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.tuml.runtime.collection.TinkerBag;
import org.tuml.runtime.collection.TinkerCollection;
import org.tuml.runtime.collection.TinkerOrderedSet;
import org.tuml.runtime.collection.TinkerSequence;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.ocl.BodyExpressionEvaluator;
import org.tuml.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.tuml.runtime.collection.ocl.IterateExpressionAccumulator;
import org.tuml.runtime.collection.ocl.OclStdLibCollection;

public class TumlCollections {

	public static final TinkerSet EMPTY_TUML_SET = new EmptySet<Object>();
	
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
	    public boolean equals(OclStdLibCollection<E> o) {
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
			return null;
		}

		@Override
		public E any(BooleanExpressionEvaluator<E> e) {
			return null;
		}

		@Override
		public TinkerSet<E> asSet() {
			return null;
		}

		@Override
		public TinkerOrderedSet<E> asOrderedSet() {
			return null;
		}

		@Override
		public TinkerSequence<E> asSequence() {
			return null;
		}

		@Override
		public TinkerBag<E> asBag() {
			return null;
		}

		@Override
		public TinkerSet<E> select(BooleanExpressionEvaluator<E> e) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <R> TinkerBag<R> collectNested(BodyExpressionEvaluator<R, E> e) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T, R> TinkerBag<T> collect(BodyExpressionEvaluator<R, E> e) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <R> TinkerSet<R> flatten() {
			// TODO Auto-generated method stub
			return null;
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
