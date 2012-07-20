package org.tuml.runtime.collection.impl;

import java.util.Collection;

import org.tuml.runtime.collection.TinkerQualifiedSequence;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.TumlNode;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class TinkerQualifiedSequenceImpl<E> extends BaseSequence<E> implements TinkerQualifiedSequence<E> {

	public TinkerQualifiedSequenceImpl(TumlNode owner, TumlRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
	}

	@Override
	public void add(int indexOf, E e) {
		maybeLoad();
		Edge edge = addToListAndListIndex(indexOf, e);
		// Can only qualify TinkerNode's
		if (!(e instanceof TumlNode)) {
			throw new IllegalStateException("Primitive properties can not be qualified!");
		}
		addQualifierToIndex(edge, (TumlNode)e);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new IllegalStateException("This method can not be called on a qualified association. Call add(E, List<Qualifier>) instead");
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new IllegalStateException("This method can not be called on a qualified association. Call add(E, List<Qualifier>) instead");
	}

	@Override
	public E set(int index, E element) {
		throw new IllegalStateException("This method can not be called on a qualified association. Call add(E, List<Qualifier>) instead");
	}

	protected void removeEdgefromIndex(Vertex v, Edge edge, int indexOf) {
		this.index.remove("index", v.getProperty("tinkerIndex"), edge);
		for (String key : edge.getPropertyKeys()) {
			if (key.startsWith("index")) {
				this.index.remove(key, edge.getProperty(key), edge);
			}
		}
	}

}
