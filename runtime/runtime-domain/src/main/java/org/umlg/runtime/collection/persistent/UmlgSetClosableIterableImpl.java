package org.umlg.runtime.collection.persistent;

import com.tinkerpop.blueprints.Edge;
import org.umlg.runtime.collection.UmlgSet;
import org.umlg.runtime.collection.UmlgRuntimeProperty;

import java.util.Iterator;

public class UmlgSetClosableIterableImpl<E> extends BaseSet<E> implements UmlgSet<E> {

	private Iterator<Edge> iterator;

	public UmlgSetClosableIterableImpl(Iterator<Edge> iterator, UmlgRuntimeProperty runtimeProperty) {
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
