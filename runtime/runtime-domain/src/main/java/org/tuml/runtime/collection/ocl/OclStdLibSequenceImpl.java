package org.tuml.runtime.collection.ocl;

import org.tuml.runtime.collection.TinkerCollection;
import org.tuml.runtime.collection.TinkerSequence;
import org.tuml.runtime.domain.ocl.OclIsInvalidException;

import java.util.*;

public class OclStdLibSequenceImpl<E> extends OclStdLibCollectionImpl<E> implements TinkerSequence<E> {

	private List<E> list;

	/**
	 * @param collection
	 * @return
	 */
	public static <E> OclStdLibSequenceImpl<E> get(Collection<E> collection) {
		return new OclStdLibSequenceImpl<E>(new ArrayList<E>(collection));
	}

	public OclStdLibSequenceImpl(List<E> list) {
		super(list);
		this.list = list;
	}

	@Override
	public Boolean equals(TinkerSequence<E> s) {
        if (size() != s.size()) {
            return false;
        }
        int count = 0;
        for (E e : s) {
            if (!e.equals(get(count++))) {
                return false;
            }
        }
		return true;
	}

	@Override
	public TinkerSequence<E> union(TinkerSequence<? extends E> s) {
		addAll(s);
		return this;
	}

	@Override
	public TinkerSequence<E> append(E object) {
        this.add(object);
        return this;
	}

	@Override
	public TinkerSequence<E> prepend(E object) {
        this.add(0, object);
        return this;
	}

	@Override
	public TinkerSequence<E> insertAt(Integer index, E object) {
        this.add(index, object);
        return this;
	}

	@Override
	public TinkerSequence<E> subSequence(Integer lower, Integer upper) {
        //Sublist excludes the upper element
        TinkerSequence<E> subList = new OclStdLibSequenceImpl(this.list.subList(lower, upper));
        subList.add(get(upper));
        return subList;
	}

	@Override
	public E at(Integer i) {
		return get(i);
	}

	@Override
	public int indexOf(Object obj) {
        return this.list.indexOf(obj);
	}

    @Override
    public E first() {
        if (this.list.isEmpty()) {
            throw new OclIsInvalidException();
        } else {
            return this.list.get(0);
        }
    }

    @Override
	public E last() {
        if (this.list.isEmpty()) {
            throw new OclIsInvalidException();
        } else {
            return this.list.get(this.list.size() - 1);
        }
	}

	@Override
	public TinkerSequence<E> including(E e) {
		if (e != null) {
			this.list.add(e);
		}
		return this;
	}

	@Override
	public TinkerSequence<E> excluding(E e) {
        if (e != null) {
            this.list.remove(e);
        }
        return this;
	}

	@Override
	public TinkerSequence<E> reverse() {
        Collections.reverse(this.list);
		return this;
	}	

	/***************************************************
	 * Iterate goodies
	 ***************************************************/
	
	@Override
	public TinkerSequence<E> select(BooleanExpressionEvaluator<E> v) {
		List<E> result = new ArrayList<E>();
		for (E e : this.collection) {
			if (v.evaluate(e)) {
				result.add(e);
			}
		}
		return new OclStdLibSequenceImpl<E>(result);
	}

	@Override
	public <R> TinkerSequence<R> collectNested(BodyExpressionEvaluator<R, E> v) {
		List<R> result = new ArrayList<R>();
		for (E e : this.list) {
			R evaluate = v.evaluate(e);
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

	@SuppressWarnings("unchecked")
	@Override
	public <R> TinkerSequence<R> flatten() {
		List<R> result = new ArrayList<R>();
		for (E e : this.list) {
			if (e instanceof TinkerCollection) {
				TinkerCollection<?> collection = (TinkerCollection<?>) e;
				result.addAll(collection.<R> flatten());
			} else {
				result.add((R) e);
			}
		}
		return new OclStdLibSequenceImpl<R>(result);
	}

	@Override
	public boolean isEmpty() {
		return this.list.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return this.list.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return this.list.iterator();
	}

	@Override
	public Object[] toArray() {
		return this.list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.list.toArray(a);
	}

	@Override
	public boolean add(E e) {
		return this.list.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return this.list.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return this.list.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return this.list.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		return this.list.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.list.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.list.retainAll(c);
	}

	@Override
	public void clear() {
		this.list.clear();
	}

	@Override
	public E get(int index) {
		return this.list.get(index);
	}

	@Override
	public E set(int index, E element) {
		return this.list.set(index, element);
	}

	@Override
	public void add(int index, E element) {
		this.list.add(index, element);
	}

	@Override
	public E remove(int index) {
		return this.list.remove(index);
	}

	@Override
	public int lastIndexOf(Object o) {
		return this.list.lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		return this.list.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return this.list.listIterator(index);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return this.list.subList(fromIndex, toIndex);
	}

	@Override
	public String toJson() {
		//TODO
		throw new RuntimeException("Not yet implemented");
	}

}
