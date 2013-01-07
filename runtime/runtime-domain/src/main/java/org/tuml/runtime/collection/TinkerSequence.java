package org.tuml.runtime.collection;

import org.tuml.runtime.collection.ocl.OclStdLibSequence;

import java.util.List;

public interface TinkerSequence<E> extends TinkerCollection<E>, List<E>, OclStdLibSequence<E> {
}
