package org.umlg.runtime.collection.ocl;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.umlg.runtime.collection.UmlgBag;
import org.umlg.runtime.collection.UmlgCollection;
import org.umlg.runtime.collection.UmlgSequence;
import org.umlg.runtime.collection.UmlgSet;
import org.umlg.runtime.collection.memory.UmlgMemoryBag;
import org.umlg.runtime.collection.memory.UmlgMemorySequence;
import org.umlg.runtime.collection.memory.UmlgMemorySet;

import java.util.*;

public class OclStdLibBagImpl<E> extends OclStdLibCollectionImpl<E> implements UmlgBag<E> {

	private Multiset<E> bag;

	/**
	 * A regular constructor compiles in eclipse but not in maven
	 * @param bag
	 * @return
	 */
	public static <E> OclStdLibBagImpl<E> get(Collection<E> bag) {
		return new OclStdLibBagImpl<E>(HashMultiset.create(bag));
	}

	public OclStdLibBagImpl(Multiset<E> bag) {
		super(bag);
		this.bag = bag;
	}

	@Override
	public Boolean equals(UmlgBag<E> bag) {
        return this.bag.equals(bag);
	}

    //Important that the result, self and bag must be the union
	@Override
	public UmlgBag<E> union(UmlgBag<E> bag) {
        UmlgBag<E> copy = new UmlgMemoryBag<E>();
        Iterator<E> iter = iterator();
        while (iter.hasNext()) {
            copy.add(iter.next());
        }
        for (E e : bag) {
            add(e);
        }
        iter = copy.iterator();
        while (iter.hasNext()) {
            bag.add(iter.next());
        }
		return this;
	}

	@Override
	public UmlgBag<E> union(UmlgSet<E> set) {
        UmlgBag<E> result = new UmlgMemoryBag<E>(this);
        for (E e : set) {
            result.add(e);
        }
        return result;
	}

	@Override
	public UmlgBag<E> intersection(UmlgBag<E> bag) {
        UmlgBag<E> result = new UmlgMemoryBag<E>();
        for (E e : bag) {
            if (contains(e)) {
                result.add(e);
            }
        }
        return result;
	}

	@Override
	public UmlgSet<E> intersection(UmlgSet<E> set) {
        UmlgSet<E> result = new UmlgMemorySet<E>();
        for (E e : bag) {
            if (contains(e)) {
                result.add(e);
            }
        }
        return result;
	}

    @Override
    public UmlgBag<E> including(E e) {
        UmlgBag<E> result = new UmlgMemoryBag<E>(this);
        if (e != null) {
            result.add(e);
        }
        return result;
    }

    @Override
    public UmlgBag<E> excluding(E e) {
        UmlgBag<E> result = new UmlgMemoryBag<E>(this);
        if (e != null) {
            result.remove(e);
        }
        return result;
    }

	/***************************************************
	 * Iterate goodies
	 ***************************************************/

	@SuppressWarnings("unchecked")
	@Override
	public <T2> UmlgBag<T2> flatten() {
		if (needsFlattening()) {
			Multiset<T2> result = HashMultiset.create();
			for (E e : this.bag) {
				if (e instanceof UmlgCollection) {
					UmlgCollection<?> collection = (UmlgCollection<?>) e;
					result.addAll(collection.<T2> flatten());
				} else {
					result.add((T2) e);
				}
			}
			return new OclStdLibBagImpl<T2>(result);
		} else {
			return OclStdLibBagImpl.<T2>get((Collection<T2>) this.bag);
		}
	}
	
	/***************************************************
	 * Iterate goodies
	 ***************************************************/
	
	@Override
	public UmlgBag<E> select(BooleanExpressionEvaluator<E> v) {
		Multiset<E> result = HashMultiset.create();
		for (E e : this.collection) {
			if (v.evaluate(e)) {
				result.add(e);
			}
		}
		return new OclStdLibBagImpl<E>(result);
	}

	@Override
	public <R> UmlgBag<R> collectNested(BodyExpressionEvaluator<R, E> v) {
		Multiset<R> result = HashMultiset.create();
		for (E e : this.bag) {
			R evaluate = v.evaluate(e);
			if (evaluate != null) {
				result.add(evaluate);
			}
		}
		return new OclStdLibBagImpl<R>(result);
	}

	@Override
	public <T, R> UmlgBag<T> collect(BodyExpressionEvaluator<R, E> v) {
		return collectNested(v).<T> flatten();
	}

	private boolean needsFlattening() {
		return !this.bag.isEmpty() && this.bag.iterator().next() instanceof UmlgCollection;
	}

	@Override
	public int count(Object element) {
		return this.bag.count(element);
	}

	@Override
	public int add(E element, int occurrences) {
		return this.bag.add(element, occurrences);
	}

	@Override
	public int remove(Object element, int occurrences) {
		return this.bag.remove(element, occurrences);
	}

	@Override
	public int setCount(E element, int count) {
		return this.bag.setCount(element, count);
	}

	@Override
	public boolean setCount(E element, int oldCount, int newCount) {
		return this.bag.setCount(element, oldCount, newCount);
	}

	@Override
	public Set<E> elementSet() {
		return this.bag.elementSet();
	}

	@Override
	public Set<com.google.common.collect.Multiset.Entry<E>> entrySet() {
		return this.bag.entrySet();
	}

	@Override
	public Iterator<E> iterator() {
		return this.bag.iterator();
	}

	@Override
	public boolean contains(Object element) {
		return this.bag.contains(element);
	}

	@Override
	public boolean containsAll(Collection<?> elements) {
		return this.bag.containsAll(elements);
	}

    @Override
    public boolean inverseAdder(E element) {
        return this.bag.add(element);
    }



	@Override
	public boolean add(E element) {
		return this.bag.add(element);
	}

	@Override
	public boolean remove(Object element) {
		return this.bag.remove(element);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.bag.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.bag.retainAll(c);
	}

	@Override
	public boolean isEmpty() {
		return this.bag.isEmpty();
	}

	@Override
	public Object[] toArray() {
		return this.bag.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.bag.toArray(a);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return this.bag.addAll(c);
	}

	@Override
	public void clear() {
		this.bag.clear();
	}

	@Override
	public String toJson() {
		throw new RuntimeException("Not yet implemented");
	}

	//Predefined Iterator Expressions
	@Override
	public UmlgSequence<E> sortedBy(Comparator<E> comparator) {
		ArrayList<E> list = new ArrayList<>(this.bag);
		Collections.sort(list, comparator);
		UmlgSequence<E> result = new UmlgMemorySequence<>(list);
		return result;
	}

}
