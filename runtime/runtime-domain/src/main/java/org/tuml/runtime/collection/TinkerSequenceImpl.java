package org.tuml.runtime.collection;

import java.util.ArrayList;
import java.util.Collection;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.domain.TinkerNode;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;

public class TinkerSequenceImpl<E> extends BaseSequence<E> implements TinkerSequence<E> {

	public TinkerSequenceImpl(TinkerNode owner, String uid, TumlRuntimeProperty multiplicity) {
		super();
		this.internalCollection = new ArrayList<E>();
		this.owner = owner;
		this.vertex = owner.getVertex();
		this.parentClass = owner.getClass();
		this.index = GraphDb.getDb().getIndex(uid + ":::" + getLabel(), Edge.class);
		if (this.index == null) {
			this.index = GraphDb.getDb().createManualIndex(uid + ":::" + getLabel(), Edge.class);
		}
		this.tumlRuntimeProperty = multiplicity;
	}

	@Override
	public boolean add(E e) {
		maybeCallInit(e);
		maybeLoad();
		boolean result = this.getInternalList().add(e);
		if (result) {
			Edge edge = addInternal(e);
			this.index.put("index", new Float(this.getInternalList().size() - 1), edge);
			getVertexForDirection(edge).setProperty("tinkerIndex", new Float(this.getInternalList().size() - 1));
		}
		return result;
	}

	@Override
	public void add(int indexOf, E e) {
		maybeCallInit(e);
		maybeLoad();
		addToListAndListIndex(indexOf, e);
	}


	@Override
	public boolean addAll(Collection<? extends E> c) {
		maybeLoad();
		boolean result = true;
		for (E e : c) {
			if (!this.add(e)) {
				result = false;
			}
		}
		return result;
	}


	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		maybeLoad();
		int indexOf = index;
		for (E e : c) {
			this.add(indexOf++, e);
		}
		return true;
	}

	@Override
	public E set(int index, E element) {
		E removedElement = this.remove(index);
		this.add(index, element);
		return removedElement;
	}

	protected void removeEdgefromIndex(Vertex v, Edge edge, int indexOf) {
		this.index.remove("index", v.getProperty("tinkerIndex"), edge);
	}

}
