package org.umlg.runtime.collection;

import com.google.common.collect.Multiset;
import org.umlg.runtime.collection.ocl.OclStdLibBag;

public interface UmlgBag<E> extends UmlgCollection<E>, Multiset<E>, OclStdLibBag<E> {
}
