package org.umlg.runtime.collection.ocl;

import org.apache.commons.collections4.set.ListOrderedSet;
import org.umlg.runtime.collection.UmlgCollection;
import org.umlg.runtime.collection.UmlgOrderedSet;
import org.umlg.runtime.collection.UmlgSequence;
import org.umlg.runtime.collection.memory.UmlgMemoryOrderedSet;
import org.umlg.runtime.domain.ocl.OclIsInvalidException;

import java.util.*;

public class OclStdLibOrderedSetImpl<E> extends OclStdLibCollectionImpl<E> implements UmlgOrderedSet<E> {

	private ListOrderedSet orderedSet;

	/**
	 * A regular constructor compiles in eclipse but not in maven
	 * @param collection
	 * @return
	 */
	public static <E> OclStdLibOrderedSetImpl<E> get(Collection<E> collection) {
		return new OclStdLibOrderedSetImpl<E>(ListOrderedSet.listOrderedSet(new ArrayList<>(collection)));
	}
	
	@SuppressWarnings("unchecked")
	public OclStdLibOrderedSetImpl(ListOrderedSet orderedSet) {
		super(orderedSet);
		this.orderedSet = orderedSet;
	}
	
	@Override
	public UmlgOrderedSet<E> append(E e) {
        UmlgOrderedSet<E> result = new UmlgMemoryOrderedSet<E>(this);
        result.add(e);
        return result;
	}

	@Override
	public UmlgOrderedSet<E> prepend(E e) {
        UmlgOrderedSet<E> result = new UmlgMemoryOrderedSet<E>(this);
        result.add(0, e);
        return result;
	}

	@Override
	public UmlgOrderedSet<E> insertAt(Integer index, E e) {
        UmlgOrderedSet<E> result = new UmlgMemoryOrderedSet<E>(this);
        result.add(index, e);
        return result;
	}

	@Override
	public UmlgOrderedSet<E> subOrderedSet(Integer lower, Integer upper) {
        //Sublist excludes the upper element
        UmlgOrderedSet<E> subList = OclStdLibOrderedSetImpl.get(this.orderedSet.asList().subList(lower, upper));
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
	public UmlgOrderedSet<E> reverse() {
        List<E> result = new ArrayList<E>(this);
        Collections.reverse(result);
        return new UmlgMemoryOrderedSet(result);
	}

	
	/***************************************************
	 * Iterate goodies
	 ***************************************************/
	
	@Override
	public UmlgOrderedSet<E> select(BooleanExpressionEvaluator<E> v) {
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
	public <R> UmlgSequence<R> collectNested(BodyExpressionEvaluator<R, E> v) {
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
	public <T, R> UmlgSequence<T> collect(BodyExpressionEvaluator<R, E> v) {
		return collectNested(v).flatten();
	}

	@Override
	public <R> UmlgSequence<R> flatten() {
        List<R> result = new ArrayList<R>();
		for (Object e : this.orderedSet) {
			if (e instanceof UmlgCollection) {
				UmlgCollection<?> collection = (UmlgCollection<?>) e;
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
    public boolean inverseAdder(E e) {
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
	public UmlgOrderedSet<E> including(E e) {
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

	//Predefined Iterator Expressions
	@Override
	public UmlgOrderedSet<E> sortedBy(Comparator<E> comparator) {
		List<E> list = new ArrayList<>(this.orderedSet);
		Collections.sort(list, comparator);
		UmlgOrderedSet<E> result = new UmlgMemoryOrderedSet<>(list);
		return result;
	}
}
