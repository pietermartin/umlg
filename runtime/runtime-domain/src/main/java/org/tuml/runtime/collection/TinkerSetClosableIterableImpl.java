package org.tuml.runtime.collection;

import com.tinkerpop.blueprints.CloseableIterable;
import com.tinkerpop.blueprints.Edge;

public class TinkerSetClosableIterableImpl<E> extends BaseSet<E> implements TinkerSet<E> {

	private CloseableIterable<Edge> closeableIterable;

	public TinkerSetClosableIterableImpl(CloseableIterable<Edge> closeableSequence) {
		super();
		this.closeableIterable = closeableSequence;
	}

	@Override
	protected Iterable<Edge> getEdges() {
		return this.closeableIterable;
	}

	@Override
	public boolean add(E e) {
		throw new IllegalStateException("This set is read only! It is constructed from a indexed search result");
	}

	@Override
	public boolean remove(Object o) {
		throw new IllegalStateException("This set is read only! It is constructed from a indexed search result");
	}

}
