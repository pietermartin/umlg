package org.umlg.runtime.collection;

import org.umlg.runtime.collection.ocl.OclStdLibOrderedSet;

import java.util.List;
import java.util.Set;

public interface UmlgOrderedSet<E> extends UmlgCollection<E>, Set<E>, List<E>, OclStdLibOrderedSet<E> {

}
