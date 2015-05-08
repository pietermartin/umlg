package org.umlg.runtime.collection.memory;

import org.umlg.runtime.collection.UmlgQualifiedSet;
import org.umlg.runtime.collection.UmlgSet;

import java.util.Collection;

/**
 * This class never really gets used.
 * It is required for the fake getter on derived unions.
 * If the particular getter is not overridden then the set will be empty.
 *
 * @param <E>
 */
public class UmlgQualifiedMemorySet<E> extends UmlgMemorySet<E> implements UmlgSet<E>, UmlgQualifiedSet<E> {

    public UmlgQualifiedMemorySet() {
        super();
    }

    public UmlgQualifiedMemorySet(Collection<E> c) {
        super(c);
    }
}
