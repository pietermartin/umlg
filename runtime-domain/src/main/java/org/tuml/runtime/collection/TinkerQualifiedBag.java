package org.tuml.runtime.collection;

import java.util.List;

import com.google.common.collect.Multiset;


public interface TinkerQualifiedBag<E> extends Multiset<E> {
	public boolean add(E e, List<Qualifier> qualifiers);
}
