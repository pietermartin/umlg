package org.tuml.runtime.collection;

import java.util.Collection;

import org.tuml.runtime.collection.ocl.OclStdLibCollection;

public interface TinkerCollection<E> extends Collection<E>, OclStdLibCollection<E> {
	String toJson();
}
