package org.tuml.runtime.collection;

import java.util.HashSet;

import com.tinkerpop.blueprints.CloseableIterable;
import com.tinkerpop.blueprints.Edge;

public class TinkerSetClosableSequenceImpl<E> extends BaseSet<E> implements TinkerSet<E> {

	private CloseableIterable<Edge> closeableIterable;

	public TinkerSetClosableSequenceImpl(CloseableIterable<Edge> closeableSequence) {
		super();
		this.internalCollection = new HashSet<E>();
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
