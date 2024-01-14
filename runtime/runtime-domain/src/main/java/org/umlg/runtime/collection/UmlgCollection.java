package org.umlg.runtime.collection;

import org.umlg.runtime.collection.ocl.OclStdLibCollection;

import java.util.Collection;
import java.util.Comparator;

public interface UmlgCollection<E> extends Collection<E>, OclStdLibCollection<E> {
    String ASSOCIATION_CLASS_VERTEX_ID = "associationClassVertexId";
    String ASSOCIATION_CLASS_EDGE_ID = "associationClassEdgeId";

    boolean inverseAdder(E e);

    default boolean addIgnoreInverse(E e) {
        throw new IllegalStateException("addIgnoreInverse() is only supported by a persistent collection BaseCollection!");
    }

    UmlgCollection<E> sortedBy(Comparator<E> e);

    public default void z_internalAdder(E e) {

    }

    public default void z_internalClear() {

    }

    public default boolean isLoaded() {
        return true;
    }

    public default void setLoaded(boolean loaded) {
    }

}
