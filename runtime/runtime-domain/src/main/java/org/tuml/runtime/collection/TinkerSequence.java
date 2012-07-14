package org.tuml.runtime.collection;

import java.util.List;

import org.tuml.runtime.collection.ocl.OclStdLibSequence;

public interface TinkerSequence<E> extends TinkerCollection<E>, List<E>, OclStdLibSequence<E> {
}
