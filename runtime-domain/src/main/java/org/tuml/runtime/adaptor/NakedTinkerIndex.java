package org.tuml.runtime.adaptor;

import com.tinkerpop.blueprints.pgm.CloseableSequence;
import com.tinkerpop.blueprints.pgm.Element;
import com.tinkerpop.blueprints.pgm.Index;

public interface NakedTinkerIndex<T extends Element> extends Index<T> {
	CloseableSequence<T> queryList(Float from, boolean minInclusive, boolean reversed);
	CloseableSequence<T> get(Float value);
}
