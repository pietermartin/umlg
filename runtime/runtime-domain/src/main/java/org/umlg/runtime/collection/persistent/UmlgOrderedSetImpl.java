package org.umlg.runtime.collection.persistent;

import org.umlg.runtime.collection.UmlgOrderedSet;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.domain.UmlgNode;

import java.util.Collection;

public class UmlgOrderedSetImpl<E> extends UmlgBaseOrderedSet<E> implements UmlgOrderedSet<E> {

    public UmlgOrderedSetImpl(UmlgNode owner, PropertyTree propertyTree) {
        super(owner, propertyTree);
    }

    public UmlgOrderedSetImpl(UmlgNode owner, PropertyTree propertyTree, boolean loaded) {
        super(owner, propertyTree, loaded);
    }

    @SuppressWarnings("unchecked")
    public UmlgOrderedSetImpl(UmlgNode owner, UmlgRuntimeProperty runtimeProperty) {
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
        if (indexOf == getInternalList().size()) {
            add(e);
        } else if (indexOf > getInternalList().size()) {
            throw new IndexOutOfBoundsException("Index: " + indexOf + ", Size: " + getInternalList().size());
        } else if (!this.getInternalListOrderedSet().contains(e)) {
            if (isEmbedded()) {
                //The BaseCollection.addInternal expects the element to have been loaded already
                if (this.loaded) {
                    getInternalList().add(indexOf, e);
                }
                addInternal(e);
            } else {
                this.edge = addToListAtIndex(indexOf, e);
                if (this.loaded) {
                    getInternalList().add(indexOf, e);
                }
                if (isInverseOrdered()) {
                    this.addToInverseLinkedList(this.edge);
                }
            }
        }
    }

    @Override
    public E set(int index, E element) {
        E removedElement = this.remove(index);
        this.add(index, element);
        return removedElement;
    }

}
