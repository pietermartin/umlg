package org.umlg.runtime.collection.ocl;

import org.umlg.runtime.collection.UmlgCollection;
import org.umlg.runtime.collection.UmlgSequence;
import org.umlg.runtime.collection.memory.UmlgMemorySequence;
import org.umlg.runtime.domain.ocl.OclIsInvalidException;

import java.util.*;

public class OclStdLibSequenceImpl<E> extends OclStdLibCollectionImpl<E> implements UmlgSequence<E> {

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
	public Boolean equals(UmlgSequence<E> s) {
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
	public UmlgSequence<E> union(UmlgSequence<? extends E> s) {
        UmlgSequence<E> result = new UmlgMemorySequence<E>(this);
        result.addAll(s);
		return result;
	}

	@Override
	public UmlgSequence<E> append(E object) {
        UmlgSequence<E> result = new UmlgMemorySequence<E>(this);
        result.add(object);
        return result;
	}

	@Override
	public UmlgSequence<E> prepend(E object) {
        UmlgSequence<E> result = new UmlgMemorySequence<E>(this);
        result.add(0, object);
        return result;
	}

	@Override
	public UmlgSequence<E> insertAt(Integer index, E object) {
        UmlgSequence<E> result = new UmlgMemorySequence<E>(this);
        result.add(index, object);
        return result;
	}

	@Override
	public UmlgSequence<E> subSequence(Integer lower, Integer upper) {
        //Sublist excludes the upper element
        UmlgSequence<E> subList = OclStdLibSequenceImpl.get(this.list.subList(lower, upper));
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
	public UmlgSequence<E> including(E e) {
        UmlgSequence<E> result = new UmlgMemorySequence<E>(this);
		if (e != null) {
			result.add(e);
		}
		return result;
	}

	@Override
	public UmlgSequence<E> excluding(E e) {
        UmlgSequence<E> result = new UmlgMemorySequence<E>(this);
        if (e != null) {
            result.remove(e);
        }
        return result;
	}

	@Override
	public UmlgSequence<E> reverse() {
        Collections.reverse(this.list);
		return this;
	}	

	/***************************************************
	 * Iterate goodies
	 ***************************************************/
	
	@Override
	public UmlgSequence<E> select(BooleanExpressionEvaluator<E> v) {
		List<E> result = new ArrayList<E>();
		for (E e : this.collection) {
			if (v.evaluate(e)) {
				result.add(e);
			}
		}
		return new OclStdLibSequenceImpl<E>(result);
	}

	@Override
	public <R> UmlgSequence<R> collectNested(BodyExpressionEvaluator<R, E> v) {
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
	public <T, R> UmlgSequence<T> collect(BodyExpressionEvaluator<R, E> v) {
		return collectNested(v).flatten();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> UmlgSequence<R> flatten() {
		List<R> result = new ArrayList<R>();
		for (E e : this.list) {
			if (e instanceof UmlgCollection) {
				UmlgCollection<?> collection = (UmlgCollection<?>) e;
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
    public boolean inverseAdder(E e) {
        return this.list.add(e);
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

	//Predefined Iterator Expressions
	@Override
	public UmlgSequence<E> sortedBy(Comparator<E> comparator) {
		List<E> list = new ArrayList<>(this.list);
		Collections.sort(list, comparator);
		UmlgSequence<E> result = new UmlgMemorySequence<>(list);
		return result;
	}
}
