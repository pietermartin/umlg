package org.umlg.runtime.collection.persistent;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.umlg.runtime.collection.UmlgQualifiedSequence;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.domain.UmlgNode;

import java.util.Collection;

public class UmlgQualifiedSequenceImpl<E> extends BaseSequence<E> implements UmlgQualifiedSequence<E> {

	public UmlgQualifiedSequenceImpl(UmlgNode owner, PropertyTree propertyTree) {
		super(owner, propertyTree);
	}

	public UmlgQualifiedSequenceImpl(UmlgNode owner, UmlgRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
    }

	@Override
	public void add(int indexOf, E e) {
        //Do not load, it needs to be traversed every time
        //It needs to be traversed because it is not possible to find the hyper vertex via the index because of duplicates
        Edge edge = addToListAtIndex(indexOf, e);
		// Can only qualify TinkerNode's
		if (!(e instanceof UmlgNode)) {
			throw new IllegalStateException("Primitive properties can not be qualified!");
		}
        if (this.loaded) {
            getInternalList().add(indexOf, e);
        }
        if (!isInverseUnique()) {
            this.addToInverseLinkedList(edge);
        }
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new IllegalStateException("This method can not be called on a qualified association. Call add(E, List<Qualifier>) instead");
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new IllegalStateException("This method can not be called on a qualified association. Call add(E, List<Qualifier>) instead");
	}

	@Override
	public E set(int index, E element) {
		throw new IllegalStateException("This method can not be called on a qualified association. Call add(E, List<Qualifier>) instead");
	}

}
