package org.umlg.runtime.adaptor;

import com.tinkerpop.blueprints.CloseableIterable;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Index;

public interface TumlTinkerIndex<T extends Element> extends Index<T> {
    /**
     * This queries the index 'index', used in sequences
     *
     * @param from
     * @param minInclusive include the from or not
     * @param reversed indicates the sorting
     * @return
     */
	CloseableIterable<T> queryList(Float from, boolean minInclusive, boolean reversed);
//	CloseableIterable<T> get(Float value);
	T getEdgeToLastElementInSequence();
}
