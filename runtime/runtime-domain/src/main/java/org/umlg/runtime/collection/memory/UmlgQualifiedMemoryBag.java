package org.umlg.runtime.collection.memory;

import org.umlg.runtime.collection.UmlgBag;
import org.umlg.runtime.collection.UmlgQualifiedBag;

import java.util.Collection;

/**
 * This class never really gets used.
 * It is required for the fake getter on derived unions.
 * If the particular getter is not overridden then the set will be empty.
 *
 * @param <E>
 */
public class UmlgQualifiedMemoryBag<E> extends UmlgMemoryBag<E> implements UmlgBag<E>, UmlgQualifiedBag<E> {

    public UmlgQualifiedMemoryBag() {
        super();
    }

    public UmlgQualifiedMemoryBag(Collection<E> c) {
        super(c);
    }
}
