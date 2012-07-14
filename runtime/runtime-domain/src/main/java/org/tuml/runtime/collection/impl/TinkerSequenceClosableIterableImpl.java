package org.tuml.runtime.collection.impl;

import java.util.Collection;
import java.util.Iterator;

import org.tuml.runtime.collection.TinkerSequence;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.TinkerNode;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class TinkerSequenceClosableIterableImpl<E> extends BaseSequence<E> implements TinkerSequence<E> {

	private Iterator<Edge> closeableIterator;

	public TinkerSequenceClosableIterableImpl(Iterator<Edge> closeableIterator, TumlRuntimeProperty tumlRuntimeProperty) {
		super(tumlRuntimeProperty);
		this.closeableIterator = closeableIterator;
	}

	@Override
	protected Iterator<Edge> getEdges() {
		return this.closeableIterator;
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

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void loadFromVertex() {
		for (Iterator<Edge> iter = getEdges(); iter.hasNext(); ) {
			Edge edge = iter.next();
			E node = null;
			try {
				Class<?> c = this.getClassToInstantiate(edge);
				if (c.isEnum()) {
					Object value = this.getVertexForDirection(edge).getProperty("value");
					node = (E) Enum.valueOf((Class<? extends Enum>) c, (String) value);
					this.internalVertexMap.put(value, this.getVertexForDirection(edge));
				} else if (TinkerNode.class.isAssignableFrom(c)) {
					node = (E) c.getConstructor(Vertex.class).newInstance(this.getVertexForDirection(edge));
				} else {
					Object value = this.getVertexForDirection(edge).getProperty("value");
					node = (E) value;
					this.internalVertexMap.put(value, this.getVertexForDirection(edge));
				}
				this.internalCollection.add(node);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}

}
