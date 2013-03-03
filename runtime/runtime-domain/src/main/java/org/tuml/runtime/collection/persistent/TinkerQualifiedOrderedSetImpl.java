package org.tuml.runtime.collection.persistent;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.collection.TinkerQualifiedOrderedSet;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.TumlNode;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;

public class TinkerQualifiedOrderedSetImpl<E> extends TumlBaseOrderedSet<E> implements TinkerQualifiedOrderedSet<E> {

    @SuppressWarnings("unchecked")
    public TinkerQualifiedOrderedSetImpl(TumlNode owner, TumlRuntimeProperty runtimeProperty) {
        super(owner, runtimeProperty);
        this.index = GraphDb.getDb().getIndex(owner.getUid() + INDEX_SEPARATOR + getQualifiedName(), Edge.class);
        if (this.index == null) {
            this.index = GraphDb.getDb().createIndex(owner.getUid() + INDEX_SEPARATOR + getQualifiedName(), Edge.class);
        }
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
