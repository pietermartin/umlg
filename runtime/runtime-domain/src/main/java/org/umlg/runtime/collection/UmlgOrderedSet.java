package org.umlg.runtime.collection;

import org.umlg.runtime.collection.ocl.OclStdLibOrderedSet;

import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;

public interface UmlgOrderedSet<E> extends UmlgCollection<E>, Set<E>, List<E>, OclStdLibOrderedSet<E> {

    @Override
    default Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, Spliterator.DISTINCT);
    }

    @Override
    boolean add(E e);

}
