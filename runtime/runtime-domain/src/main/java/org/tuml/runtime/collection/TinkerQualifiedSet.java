package org.tuml.runtime.collection;

import java.util.List;


public interface TinkerQualifiedSet<E> extends TinkerSet<E> {
	public boolean add(E e, List<Qualifier> qualifiers);
}
