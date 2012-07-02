package org.tuml.runtime.collection;

import java.util.Collection;

import com.tinkerpop.blueprints.CloseableIterable;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class TinkerSequenceClosableIterableImpl<E> extends BaseSequence<E> implements TinkerSequence<E> {

	private CloseableIterable<Edge> closeableIterable;

	public TinkerSequenceClosableIterableImpl(CloseableIterable<Edge> closeableSequence) {
		super();
		this.closeableIterable = closeableSequence;
	}

	@Override
	protected Iterable<Edge> getEdges() {
		return this.closeableIterable;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new IllegalStateException("This set is read only! It is constructed from a indexed search result");
	}

	@Override
	public E set(int index, E element) {
		throw new IllegalStateException("This set is read only! It is constructed from a indexed search result");
	}

	@Override
	public void add(int index, E element) {
		throw new IllegalStateException("This set is read only! It is constructed from a indexed search result");
	}

	@Override
	protected void removeEdgefromIndex(Vertex v, Edge edge, int indexOf) {
		throw new IllegalStateException("This set is read only! It is constructed from a indexed search result");
	}

}
