package org.umlg.runtime.collection.memory;

import org.umlg.runtime.collection.UmlgQualifiedSequence;
import org.umlg.runtime.collection.UmlgSequence;

import java.util.Collection;

/**
 * This class never really gets used.
 * It is required for the fake getter on derived unions.
 * If the particular getter is not overridden then the set will be empty.
 *
 * @param <E>
 */
public class UmlgQualifiedMemorySequence<E> extends UmlgMemorySequence<E> implements UmlgSequence<E>, UmlgQualifiedSequence<E> {

    public UmlgQualifiedMemorySequence() {
        super();
    }

    public UmlgQualifiedMemorySequence(Collection<E> c) {
        super(c);
    }

}
