package org.umlg.runtime.collection.persistent;

import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.collection.UmlgSequence;
import org.umlg.runtime.domain.UmlgNode;

import java.util.Collection;

public class UmlgSequenceImpl<E> extends BaseSequence<E> implements UmlgSequence<E> {

	public UmlgSequenceImpl(UmlgNode owner, UmlgRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
	}

	@Override
	public void add(int indexOf, E e) {
        //Do not load, it needs to be traversed every time
        //It needs to be traversed because it is not possible to find the hyper vertex via the index because of duplicates
		this.edge = addToListAtIndex(indexOf, e);
        if (this.loaded) {
            getInternalList().add(indexOf, e);
        }
        if (!isInverseUnique()) {
            this.addToInverseLinkedList(this.edge);
        }
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		maybeLoad();
		boolean result = true;
		for (E e : c) {
			if (!this.add(e)) {
				result = false;
			}
		}
		return result;
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
	public E set(int index, E element) {
		E removedElement = this.remove(index);
		this.add(index, element);
		return removedElement;
	}

}
