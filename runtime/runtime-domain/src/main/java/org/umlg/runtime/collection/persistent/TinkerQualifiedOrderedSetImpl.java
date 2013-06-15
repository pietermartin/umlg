package org.umlg.runtime.collection.persistent;

import com.tinkerpop.blueprints.Edge;
import org.umlg.runtime.adaptor.GraphDb;
import org.umlg.runtime.collection.TinkerQualifiedOrderedSet;
import org.umlg.runtime.collection.TumlRuntimeProperty;
import org.umlg.runtime.domain.TumlNode;

import java.util.Collection;

public class TinkerQualifiedOrderedSetImpl<E> extends TumlBaseOrderedSet<E> implements TinkerQualifiedOrderedSet<E> {

    @SuppressWarnings("unchecked")
    public TinkerQualifiedOrderedSetImpl(TumlNode owner, TumlRuntimeProperty runtimeProperty) {
        super(owner, runtimeProperty);
        this.index = GraphDb.getDb().getIndex(getQualifiedName(), Edge.class);
    }

    @Override
    public void add(int indexOf, E e) {
        maybeLoad();
        if (!this.getInternalListOrderedSet().contains(e)) {
            Edge edge = addToListAtIndex(indexOf, e);
            // Can only qualify TinkerNode's
            if (!(e instanceof TumlNode)) {
                throw new IllegalStateException("Primitive properties can not be qualified!");
            }
            addQualifierToIndex(edge, (TumlNode) e);
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
