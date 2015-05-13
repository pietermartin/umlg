package org.umlg.runtime.collection.ocl;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.umlg.runtime.collection.UmlgBag;
import org.umlg.runtime.collection.UmlgCollection;
import org.umlg.runtime.collection.UmlgOrderedSet;
import org.umlg.runtime.collection.UmlgSet;
import org.umlg.runtime.collection.memory.UmlgMemoryBag;
import org.umlg.runtime.collection.memory.UmlgMemoryOrderedSet;
import org.umlg.runtime.collection.memory.UmlgMemorySet;

import java.util.*;

public class OclStdLibSetImpl<E> extends OclStdLibCollectionImpl<E> implements UmlgSet<E> {

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
    public UmlgSet<E> union(UmlgSet<? extends E> s) {
        UmlgSet<E> result = new UmlgMemorySet<E>(this);
        result.addAll(s);
        return result;
    }

    @Override
    public UmlgBag<E> union(UmlgBag<? extends E> bag) {
        UmlgBag<E> result = new UmlgMemoryBag<E>(this);
        result.addAll(bag);
        return result;
    }

    @Override
    public Boolean equals(UmlgSet<E> s) {
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
    public UmlgSet<E> intersection(UmlgSet<E> s) {
        UmlgSet<E> result = new UmlgMemorySet<E>();
        for (E e : s) {
            if (contains(e)) {
                result.add(e);
            }
        }
        return result;
    }

    @Override
    public UmlgSet<E> intersection(UmlgBag<E> bag) {
        UmlgSet<E> result = new UmlgMemorySet<E>();
        for (E e : bag) {
            if (contains(e)) {
                result.add(e);
            }
        }
        return result;
    }

    @Override
    public UmlgSet<E> subtract(UmlgSet<E> s) {
        UmlgSet<E> result = new UmlgMemorySet<E>();
        Iterator<E> iter = iterator();
        while (iter.hasNext()) {
            E e = iter.next();
            if (!s.contains(e)) {
                result.add(e);
            }
        }
        return result;
    }

    @Override
    public UmlgSet<E> including(E e) {
        UmlgSet<E> result = new UmlgMemorySet<E>(this);
        result.add(e);
        return result;
    }

    @Override
    public UmlgSet<E> excluding(E e) {
        UmlgSet<E> result = new UmlgMemorySet<E>(this);
        result.remove(e);
        return result;
    }

    @Override
    public UmlgSet<E> symmetricDifference(UmlgSet<E> s) {
        UmlgSet<E> result = new UmlgMemorySet<E>();
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
    public UmlgSet<E> select(BooleanExpressionEvaluator<E> v) {
        Set<E> result = new HashSet<E>();
        for (E e : this.collection) {
            if (v.evaluate(e)) {
                result.add(e);
            }
        }
        return new OclStdLibSetImpl<E>(result);
    }

    @Override
    public <R> UmlgBag<R> collectNested(BodyExpressionEvaluator<R, E> v) {
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
    public <T, R> UmlgBag<T> collect(BodyExpressionEvaluator<R, E> v) {
        return collectNested(v).flatten();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> UmlgSet<R> flatten() {
        Set<R> result = new HashSet<R>();
        for (E e : this.set) {
            if (e instanceof UmlgCollection) {
                UmlgCollection<?> collection = (UmlgCollection<?>) e;
                result.addAll(collection.<R>flatten());
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


    //Predefined Iterator Expressions
    @Override
    public UmlgOrderedSet<E> sortedBy(Comparator<E> comparator) {
        List<E> list = new ArrayList<>(this.set);
        Collections.sort(list, comparator);
        UmlgOrderedSet<E> result = new UmlgMemoryOrderedSet<>(list);
        return result;
    }
}
