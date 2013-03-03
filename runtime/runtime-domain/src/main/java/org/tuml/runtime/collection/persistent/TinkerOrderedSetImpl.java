package org.tuml.runtime.collection.persistent;

import org.tuml.runtime.collection.TinkerOrderedSet;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.TumlNode;

import java.util.Collection;

public class TinkerOrderedSetImpl<E> extends TumlBaseOrderedSet<E> implements TinkerOrderedSet<E> {


    @SuppressWarnings("unchecked")
    public TinkerOrderedSetImpl(TumlNode owner, TumlRuntimeProperty runtimeProperty) {
        super(owner, runtimeProperty);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        maybeLoad();
        int indexOf = index;
        for (E e : c) {
            this.add(indexOf++, e);
        }
        return true;
    }

    @Override
    public void add(int indexOf, E e) {
        maybeLoad();
        if (!this.getInternalListOrderedSet().contains(e)) {
            addToListAtIndex(indexOf, e);
        }
    }

    @Override
    public E set(int index, E element) {
        E removedElement = this.remove(index);
        this.add(index, element);
        return removedElement;
    }

}
