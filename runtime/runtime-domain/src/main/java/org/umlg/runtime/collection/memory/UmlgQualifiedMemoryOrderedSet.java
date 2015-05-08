package org.umlg.runtime.collection.memory;

import org.umlg.runtime.collection.UmlgOrderedSet;
import org.umlg.runtime.collection.UmlgQualifiedOrderedSet;

import java.util.Collection;
import java.util.Spliterator;
import java.util.Spliterators;

/**
 * This class never really gets used.
 * It is required for the fake getter on derived unions.
 * If the particular getter is not overridden then the set will be empty.
 *
 * @param <E>
 */
public class UmlgQualifiedMemoryOrderedSet<E> extends UmlgMemoryOrderedSet<E> implements UmlgOrderedSet<E>, UmlgQualifiedOrderedSet<E> {

    @SuppressWarnings("unchecked")
    public UmlgQualifiedMemoryOrderedSet() {
        super();
    }

    @SuppressWarnings("unchecked")
    public UmlgQualifiedMemoryOrderedSet(Collection<E> c) {
        super(c);
    }

    @Override
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, Spliterator.DISTINCT);
    }
}
