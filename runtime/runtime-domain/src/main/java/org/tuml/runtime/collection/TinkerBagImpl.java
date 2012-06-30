package org.tuml.runtime.collection;

import org.tuml.runtime.domain.TinkerNode;

import com.tinkerpop.blueprints.Edge;

public class TinkerBagImpl<E> extends BaseBag<E> implements TinkerBag<E> {

	public TinkerBagImpl(TinkerNode owner, TumlRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
	}

	@Override
	protected void doWithEdgeAfterAddition(Edge edge, E e) {
	}

}
