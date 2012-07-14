package org.tuml.runtime.collection;

import java.util.List;
import java.util.Set;

import org.tuml.runtime.collection.ocl.OclStdLibOrderedSet;

public interface TinkerQualifiedOrderedSet<E> extends Set<E>, List<E>, OclStdLibOrderedSet<E> {
}
