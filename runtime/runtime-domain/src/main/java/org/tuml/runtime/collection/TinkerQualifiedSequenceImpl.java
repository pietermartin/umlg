package org.tuml.runtime.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.domain.TinkerNode;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class TinkerQualifiedSequenceImpl<E> extends BaseSequence<E> implements TinkerQualifiedSequence<E> {

	public TinkerQualifiedSequenceImpl(TinkerNode owner, String uid, TumlRuntimeProperty multiplicity) {
		super();
		this.internalCollection = new ArrayList<E>();
		this.owner = owner;
		this.vertex = owner.getVertex();
		this.parentClass = owner.getClass();
		this.tumlRuntimeProperty = multiplicity;
		this.index = GraphDb.getDb().getIndex(uid + ":::" + getLabel(), Edge.class);
		if (this.index == null) {
			this.index = GraphDb.getDb().createIndex(uid + ":::" + getLabel(), Edge.class);
		}
	}

	@Override
	public boolean add(E e, List<Qualifier> qualifiers) {
		maybeCallInit(e);
		maybeLoad();
		validateQualifiedMultiplicity(qualifiers);
		boolean result = this.getInternalList().add(e);
		if (result) {
			Edge edge = addInternal(e);
			// Edge can only be null on isOneToMany, toOneToOne which is a
			// String, Interger, Boolean or primitive
			if (edge == null && !isOnePrimitive()) {
				throw new IllegalStateException("Edge can only be null on isOneToMany, toOneToOne which is a String, Interger, Boolean or primitive");
			}
			if (edge != null) {
				this.index.put("index", new Float(this.getInternalList().size() - 1), edge);
				getVertexForDirection(edge).setProperty("tinkerIndex", new Float(this.getInternalList().size() - 1));
				addQualifierToIndex(edge, qualifiers);
			}
		}
		return result;
	}

	@Override
	public boolean add(E e) {
		throw new IllegalStateException("This method can not be called on a qualified association. Call add(E, List<Qualifier>) instead");
	}

	@Override
	public void add(int indexOf, E e, List<Qualifier> qualifiers) {
		maybeCallInit(e);
		maybeLoad();
		Edge edge = addToListAndListIndex(indexOf, e);
		addQualifierToIndex(edge, qualifiers);
	}

	@Override
	public void add(int indexOf, E e) {
		throw new IllegalStateException("This method can not be called on a qualified association. Call add(E, List<Qualifier>) instead");
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

	protected void validateQualifiedMultiplicity(List<Qualifier> qualifiers) {
		for (Qualifier qualifier : qualifiers) {
			if (qualifier.isOne()) {
				long count = this.index.count(qualifier.getKey(), qualifier.getValue());
				if (count > 0) {
					// Add info to exception
					throw new IllegalStateException("qualifier fails, entry for qualifier already exist");
				}
			}
		}
	}

	private void addQualifierToIndex(Edge edge, List<Qualifier> qualifiers) {
		for (Qualifier qualifier : qualifiers) {
			this.index.put(qualifier.getKey(), qualifier.getValue(), edge);
			edge.setProperty("index" + qualifier.getKey(), qualifier.getValue());
		}
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
