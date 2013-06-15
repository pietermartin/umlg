package org.umlg.runtime.collection;

import com.google.common.collect.Multiset;
import org.umlg.runtime.collection.ocl.OclStdLibBag;

public interface TinkerBag<E> extends TinkerCollection<E>, Multiset<E>, OclStdLibBag<E> {
}
