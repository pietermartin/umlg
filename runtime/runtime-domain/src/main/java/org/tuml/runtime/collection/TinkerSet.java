package org.tuml.runtime.collection;

import java.util.Set;

import org.tuml.runtime.collection.ocl.OclStdLibSet;

public interface TinkerSet<E> extends TinkerCollection<E>, Set<E>, OclStdLibSet<E> {
}
