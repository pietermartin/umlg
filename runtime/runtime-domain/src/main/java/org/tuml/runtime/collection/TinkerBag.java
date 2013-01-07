package org.tuml.runtime.collection;

import com.google.common.collect.Multiset;
import org.tuml.runtime.collection.ocl.OclStdLibBag;

public interface TinkerBag<E> extends TinkerCollection<E>, Multiset<E>, OclStdLibBag<E> {
}
