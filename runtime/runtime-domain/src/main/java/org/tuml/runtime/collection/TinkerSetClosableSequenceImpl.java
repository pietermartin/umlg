package org.tuml.runtime.collection;

import java.util.HashSet;

import com.tinkerpop.blueprints.pgm.CloseableSequence;
import com.tinkerpop.blueprints.pgm.Edge;

public class TinkerSetClosableSequenceImpl<E> extends BaseSet<E> implements TinkerSet<E> {

	private CloseableSequence<Edge> closeableSequence;

	public TinkerSetClosableSequenceImpl(CloseableSequence<Edge> closeableSequence, boolean isInverse) {
		super();
		this.internalCollection = new HashSet<E>();
		this.closeableSequence = closeableSequence;
		this.inverse = isInverse;
	}

	@Override
	protected Iterable<Edge> getEdges() {
		return this.closeableSequence;
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
