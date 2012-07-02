package org.tuml.runtime.adaptor;

import com.tinkerpop.blueprints.CloseableIterable;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Index;

public interface NakedTinkerIndex<T extends Element> extends Index<T> {
	CloseableIterable<T> queryList(Float from, boolean minInclusive, boolean reversed);
	CloseableIterable<T> get(Float value);
	T getEdgeToLastElementInSequence();
}
