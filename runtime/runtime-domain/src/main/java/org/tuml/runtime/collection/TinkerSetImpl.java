package org.tuml.runtime.collection;

import java.util.Set;

import org.tuml.runtime.domain.TinkerNode;

import com.tinkerpop.blueprints.Edge;

public class TinkerSetImpl<E> extends BaseSet<E> implements TinkerSet<E> {

	public TinkerSetImpl(TinkerNode owner, TumlRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
	}
	
	public Set<E> getInternalSet() {
		return (Set<E>) this.internalCollection;
	}

	@Override
	protected void doWithEdgeAfterAddition(Edge edge, E e) {
	}

}
