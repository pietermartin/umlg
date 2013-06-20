package org.umlg.runtime.collection.ocl;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.umlg.runtime.collection.TinkerBag;
import org.umlg.runtime.collection.TinkerCollection;
import org.umlg.runtime.collection.TinkerSet;
import org.umlg.runtime.collection.memory.TumlMemoryBag;
import org.umlg.runtime.collection.memory.TumlMemorySet;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class OclStdLibSetImpl<E> extends OclStdLibCollectionImpl<E> implements TinkerSet<E> {

	private Set<E> set;

	/**
	 * A regular constructor compiles in eclipse but not in maven
	 * 
	 * @param collection
	 * @return
	 */
	public static <E> OclStdLibSetImpl<E> get(Collection<E> collection) {
		return new OclStdLibSetImpl<E>(new HashSet<E>(collection));
	}

	public OclStdLibSetImpl(Set<E> set) {
		super(set);
		this.set = set;
	}

	@Override
	public TinkerSet<E> union(TinkerSet<? extends E> s) {
        TinkerSet<E> result = new TumlMemorySet<E>(this);
        result.addAll(s);
		return result;
	}

	@Override
	public TinkerBag<E> union(TinkerBag<? extends E> bag) {
        TinkerBag<E> result = new TumlMemoryBag<E>(this);
        result.addAll(bag);
        return result;
	}

	@Override
	public Boolean equals(TinkerSet<E> s) {
        if (size() != s.size()) {
            return false;
        }
        for (E e : s) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
	}

	@Override
	public TinkerSet<E> intersection(TinkerSet<E> s) {
        TinkerSet<E> result = new TumlMemorySet<E>();
        for (E e : s) {
            if (contains(e)) {
                result.add(e);
            }
        }
        return result;
	}

	@Override
	public TinkerSet<E> intersection(TinkerBag<E> bag) {
        TinkerSet<E> result = new TumlMemorySet<E>();
        for (E e : bag) {
            if (contains(e)) {
                result.add(e);
            }
        }
        return result;
	}

	@Override
	public TinkerSet<E> subtract(TinkerSet<E> s) {
        TinkerSet<E> result = new TumlMemorySet<E>();
        Iterator<E> iter = iterator();
        while (iter.hasNext()) {
            E e =  iter.next();
            if (!s.contains(e)) {
                result.add(e);
            }
        }
        return result;
	}

	@Override
	public TinkerSet<E> including(E e) {
        TinkerSet<E> result = new TumlMemorySet<E>(this);
        result.add(e);
		return result;
	}

	@Override
	public TinkerSet<E> excluding(E e) {
        TinkerSet<E> result = new TumlMemorySet<E>(this);
		result.remove(e);
        return result;
	}

	@Override
	public TinkerSet<E> symmetricDifference(TinkerSet<E> s) {
        TinkerSet<E> result = new TumlMemorySet<E>();
        for (E e : s) {
            if (!contains(e)) {
                result.add(e);
            }
        }
        Iterator<E> iter = iterator();
        while (iter.hasNext()) {
            E e = iter.next();
            if (!s.contains(e)) {
                result.add(e);
            }
        }
        return result;
	}

	/***************************************************
	 * Iterate goodies
	 ***************************************************/

	@Override
	public TinkerSet<E> select(BooleanExpressionEvaluator<E> v) {
		Set<E> result = new HashSet<E>();
		for (E e : this.collection) {
			if (v.evaluate(e)) {
				result.add(e);
			}
		}
		return new OclStdLibSetImpl<E>(result);
	}

	@Override
	public <R> TinkerBag<R> collectNested(BodyExpressionEvaluator<R, E> v) {
		Multiset<R> result = HashMultiset.create();
		for (E e : this.set) {
			R evaluate = v.evaluate(e);
			if (evaluate != null) {
				result.add(evaluate);
			}
		}
		return new OclStdLibBagImpl<R>(result);
	}

	@Override
	public <T, R> TinkerBag<T> collect(BodyExpressionEvaluator<R, E> v) {
		return collectNested(v).flatten();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> TinkerSet<R> flatten() {
		Set<R> result = new HashSet<R>();
		for (E e : this.set) {
			if (e instanceof TinkerCollection) {
				TinkerCollection<?> collection = (TinkerCollection<?>) e;
				result.addAll(collection.<R> flatten());
			} else {
				result.add((R) e);
			}
		}
		return new OclStdLibSetImpl<R>(result);
	}

	@Override
	public boolean isEmpty() {
		return this.set.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return this.set.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return this.set.iterator();
	}

	@Override
	public Object[] toArray() {
		return this.set.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.set.toArray(a);
	}

    @Override
    public boolean inverseAdder(E e) {
        return this.set.add(e);
    }

    @Override
	public boolean add(E e) {
		return this.set.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return this.set.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return this.set.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return this.set.addAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.set.retainAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.set.removeAll(c);
	}

	@Override
	public void clear() {
		this.set.clear();
	}

	@Override
	public String toJson() {
		// TODO
		throw new RuntimeException("Not yet implemented");
	}

}
