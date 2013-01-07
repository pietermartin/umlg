package org.tuml.runtime.collection;

import org.tuml.runtime.collection.ocl.OclStdLibCollection;

import java.util.Collection;

public interface TinkerCollection<E> extends Collection<E>, OclStdLibCollection<E> {
	String toJson();
}
