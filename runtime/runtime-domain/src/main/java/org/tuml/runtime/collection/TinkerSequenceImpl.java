package org.tuml.runtime.collection;

import java.util.Collection;

import org.tuml.runtime.domain.TinkerNode;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class TinkerSequenceImpl<E> extends BaseSequence<E> implements TinkerSequence<E> {

	public TinkerSequenceImpl(TinkerNode owner, TumlRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
	}

	@Override
	public void add(int indexOf, E e) {
		// validateMultiplicityForAdditionalElement calls size() which loads the
		// collection
		validateMultiplicityForAdditionalElement();
		maybeCallInit(e);
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
