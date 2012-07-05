package org.tuml.runtime.collection;

import java.util.Iterator;

import com.tinkerpop.blueprints.Edge;

public class TinkerSetClosableIterableImpl<E> extends BaseSet<E> implements TinkerSet<E> {

	private Iterator<Edge> iterator;

	public TinkerSetClosableIterableImpl(Iterator<Edge> iterator, TumlRuntimeProperty runtimeProperty) {
		super(runtimeProperty);
		this.iterator = iterator;
	}

	@Override
	protected Iterator<Edge> getEdges() {
		return this.iterator;
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
