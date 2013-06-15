package org.umlg.runtime.collection;

import org.umlg.runtime.collection.ocl.OclStdLibCollection;

import java.util.Collection;

public interface TinkerCollection<E> extends Collection<E>, OclStdLibCollection<E> {
	String toJson();
}
