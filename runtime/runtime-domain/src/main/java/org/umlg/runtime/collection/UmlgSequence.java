package org.umlg.runtime.collection;

import org.umlg.runtime.collection.ocl.OclStdLibSequence;

import java.util.List;

public interface UmlgSequence<E> extends UmlgCollection<E>, List<E>, OclStdLibSequence<E> {
}
