package org.tuml.runtime.collection;

import org.tuml.runtime.collection.ocl.OclStdLibBag;

import com.google.common.collect.Multiset;

public interface TinkerBag<E> extends TinkerCollection<E>, Multiset<E>, OclStdLibBag<E> {
}
