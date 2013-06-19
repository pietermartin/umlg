package org.umlg.runtime.collection.ocl;

import org.apache.commons.collections.set.ListOrderedSet;
import org.umlg.runtime.collection.TinkerCollection;
import org.umlg.runtime.collection.TinkerOrderedSet;
import org.umlg.runtime.collection.TinkerSequence;
import org.umlg.runtime.collection.memory.TumlMemoryOrderedSet;
import org.umlg.runtime.domain.ocl.OclIsInvalidException;

import java.util.*;

public class OclStdLibOrderedSetImpl<E> extends OclStdLibCollectionImpl<E> implements TinkerOrderedSet<E> {

	private ListOrderedSet orderedSet;

	/**
	 * A regular constructor compiles in eclipse but not in maven
	 * @param collection
	 * @return
	 */
	public static <E> OclStdLibOrderedSetImpl<E> get(Collection<E> collection) {
		return new OclStdLibOrderedSetImpl<E>(ListOrderedSet.decorate(new ArrayList<E>(collection)));
	}
	
	@SuppressWarnings("unchecked")
	public OclStdLibOrderedSetImpl(ListOrderedSet orderedSet) {
		super(orderedSet);
		this.orderedSet = orderedSet;
	}
	
	@Override
	public TinkerOrderedSet<E> append(E e) {
        TinkerOrderedSet<E> result = new TumlMemoryOrderedSet<E>(this);
        result.add(e);
        return result;
	}

	@Override
	public TinkerOrderedSet<E> prepend(E e) {
        TinkerOrderedSet<E> result = new TumlMemoryOrderedSet<E>(this);
        result.add(0, e);
        return result;
	}

	@Override
	public TinkerOrderedSet<E> insertAt(Integer index, E e) {
        TinkerOrderedSet<E> result = new TumlMemoryOrderedSet<E>(this);
        result.add(index, e);
        return result;
	}

	@Override
	public TinkerOrderedSet<E> subOrderedSet(Integer lower, Integer upper) {
        //Sublist excludes the upper element
        TinkerOrderedSet<E> subList = OclStdLibOrderedSetImpl.get(this.orderedSet.asList().subList(lower, upper));
        subList.add(get(upper));
        return subList;
	}

	@Override
	public E at(Integer i) {
        return get(i);
	}

	@Override
	public E first() {
        if (this.orderedSet.isEmpty()) {
            throw new OclIsInvalidException();
        } else {
            return (E)this.orderedSet.get(0);
        }
	}

	@Override
	public E last() {
        if (this.orderedSet.isEmpty()) {
            throw new OclIsInvalidException();
        } else {
            return (E)this.orderedSet.get(this.orderedSet.size() - 1);
        }
	}

	@Override
	public TinkerOrderedSet<E> reverse() {
        List<E> result = new ArrayList<E>(this);
        Collections.reverse(result);
        return new TumlMemoryOrderedSet(result);
	}

	
	/***************************************************
	 * Iterate goodies
	 ***************************************************/
	
	@Override
	public TinkerOrderedSet<E> select(BooleanExpressionEvaluator<E> v) {
		ListOrderedSet result = new ListOrderedSet();
		for (E e : this.collection) {
			if (v.evaluate(e)) {
				result.add(e);
			}
		}
		return new OclStdLibOrderedSetImpl<E>(result);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> TinkerSequence<R> collectNested(BodyExpressionEvaluator<R, E> v) {
        List<R> result = new ArrayList<R>();
		for (Object e : this.orderedSet) {
			R evaluate = v.evaluate((E)e);
			if (evaluate != null) {
				result.add(evaluate);
			}
		}
		return new OclStdLibSequenceImpl<R>(result);
	}
	
	@Override
	public <T, R> TinkerSequence<T> collect(BodyExpressionEvaluator<R, E> v) {
		return collectNested(v).flatten();
	}

	@Override
	public <R> TinkerSequence<R> flatten() {
        List<R> result = new ArrayList<R>();
		for (Object e : this.orderedSet) {
			if (e instanceof TinkerCollection) {
				TinkerCollection<?> collection = (TinkerCollection<?>) e;
				result.addAll(collection.<R> flatten());
			} else {
				result.add((R)e);
			}
		}
		return new OclStdLibSequenceImpl<R>(result);
	}

	@Override
	public boolean isEmpty() {
		return this.orderedSet.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return this.orderedSet.contains(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<E> iterator() {
		return this.orderedSet.iterator();
	}

	@Override
	public Object[] toArray() {
		return this.orderedSet.toArray();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) {
		return (T[]) this.orderedSet.toArray(a);
	}

    @Override
    public boolean internalAdd(E e) {
        return this.orderedSet.add(e);
    }

    @Override
	public boolean add(E e) {
		return this.orderedSet.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return this.orderedSet.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return this.orderedSet.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return this.orderedSet.addAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.orderedSet.retainAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.orderedSet.removeAll(c);
	}

	@Override
	public void clear() {
		this.orderedSet.clear();
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		return this.orderedSet.addAll(index, c);
	}

	@SuppressWarnings("unchecked")
	@Override
	public E get(int index) {
		return (E) this.orderedSet.get(index);
	}

	@Override
	public E set(int index, E element) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public void add(int index, E element) {
		this.orderedSet.add(index, element);
	}

	@SuppressWarnings("unchecked")
	@Override
	public E remove(int index) {
		return (E) this.orderedSet.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return this.orderedSet.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public ListIterator<E> listIterator() {
		throw new RuntimeException("Not supported");
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
        return this.orderedSet.asList().subList(fromIndex, toIndex);
	}

	@Override
	public TinkerOrderedSet<E> including(E e) {
		if (e != null) {
			this.orderedSet.add(e);
		}
		return this;
	}

	@Override
	public String toJson() {
		//TODO
		throw new RuntimeException("Not yet implemented");
	}

}
