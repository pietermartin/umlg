package org.umlg.runtime.collection.memory;

import com.tinkerpop.blueprints.Vertex;
import org.umlg.runtime.adaptor.UMLG;

import java.util.*;

/**
 * Date: 2014/03/16
 * Time: 11:38 PM
 */
public class UmlgLazyList<PersistentObject> implements List {

    private List<PersistentObject> internal = new ArrayList<PersistentObject>();
    private Iterator<PersistentObject> iterator;
    private int loadedUpTo = 0;
    private boolean fullyLoaded = false;

    public UmlgLazyList(Iterator<Vertex> iterator) {
        this.iterator = new IteratorWrapper(iterator);
    }

    @Override
    public int size() {
        if (!fullyLoaded) {
            loadFully();
        }
        return loadedUpTo;
    }

    private void loadFully() {
        while (this.iterator.hasNext()) {
            this.internal.add(this.iterator.next());
        }
        this.fullyLoaded = true;
    }

    @Override
    public boolean isEmpty() {
        if (!fullyLoaded) {
            loadFully();
        }
        return this.internal.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if (!fullyLoaded) {
            loadFully();
        }
        return this.internal.contains(o);
    }

    @Override
    public Iterator iterator() {
        return this.iterator;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public PersistentObject[] toArray(Object[] a) {
        return (PersistentObject[])this.internal.toArray(a);
    }

    @Override
    public boolean add(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PersistentObject get(int index) {
        if (this.loadedUpTo > index) {
            return this.internal.get(index);
        } else {
            while (this.iterator.hasNext()) {
                this.internal.add(this.iterator.next());
                if (this.loadedUpTo == index) {
                    break;
                }
            }
            return this.internal.get(index);
        }
    }

    @Override
    public Object set(int index, Object element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, Object element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PersistentObject remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        if (!fullyLoaded) {
            loadFully();
        }
        return this.internal.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        if (!fullyLoaded) {
            loadFully();
        }
        return this.internal.lastIndexOf(o);
    }

    @Override
    public ListIterator listIterator() {
        if (!fullyLoaded) {
            loadFully();
        }
        return this.internal.listIterator();
    }

    @Override
    public ListIterator listIterator(int index) {
        if (!fullyLoaded) {
            loadFully();
        }
        return this.internal.listIterator(index);
    }

    @Override
    public List subList(int fromIndex, int toIndex) {
        if (!fullyLoaded) {
            loadFully();
        }
        return this.internal.subList(fromIndex, toIndex);
    }

    @Override
    public boolean retainAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection c) {
        if (!fullyLoaded) {
            loadFully();
        }
        return this.internal.containsAll(c);
    }

    public class IteratorWrapper implements Iterator {

        private Iterator<Vertex> iterator;

        public IteratorWrapper(Iterator<Vertex> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        @Override
        public PersistentObject next() {
            PersistentObject e = UMLG.get().instantiateClassifier(this.iterator.next().getId());
            UmlgLazyList.this.internal.add(e);
            UmlgLazyList.this.loadedUpTo++;
            return e;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}
