package org.umlg.runtime.collection;

import org.umlg.runtime.collection.ocl.OclStdLibOrderedSet;

import java.util.List;
import java.util.Set;

public interface TinkerQualifiedOrderedSet<E> extends TinkerCollection<E>, Set<E>, List<E>, OclStdLibOrderedSet<E> {
}
