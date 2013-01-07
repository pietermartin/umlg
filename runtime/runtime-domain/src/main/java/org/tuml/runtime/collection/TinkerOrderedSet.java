package org.tuml.runtime.collection;

import org.tuml.runtime.collection.ocl.OclStdLibOrderedSet;

import java.util.List;
import java.util.Set;

public interface TinkerOrderedSet<E> extends TinkerCollection<E>, Set<E>, List<E>, OclStdLibOrderedSet<E> {

}
