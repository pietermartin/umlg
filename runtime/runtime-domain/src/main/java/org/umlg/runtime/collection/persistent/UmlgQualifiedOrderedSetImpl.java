package org.umlg.runtime.collection.persistent;

import com.tinkerpop.blueprints.Edge;
import org.umlg.runtime.collection.UmlgQualifiedOrderedSet;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.domain.UmlgNode;

import java.util.Collection;

public class UmlgQualifiedOrderedSetImpl<E> extends UmlgBaseOrderedSet<E> implements UmlgQualifiedOrderedSet<E> {

    @SuppressWarnings("unchecked")
    public UmlgQualifiedOrderedSetImpl(UmlgNode owner, UmlgRuntimeProperty runtimeProperty) {
        super(owner, runtimeProperty);
//        this.index = GraphDb.getDb().getIndex(getQualifiedName(), Edge.class);
    }

    @Override
    public void add(int indexOf, E e) {
        maybeLoad();
        if (!this.getInternalListOrderedSet().contains(e)) {
            Edge edge = addToListAtIndex(indexOf, e);
            // Can only qualify TinkerNode's
            if (!(e instanceof UmlgNode)) {
                throw new IllegalStateException("Primitive properties can not be qualified!");
            }
            addQualifierToIndex(edge, (UmlgNode) e);
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
