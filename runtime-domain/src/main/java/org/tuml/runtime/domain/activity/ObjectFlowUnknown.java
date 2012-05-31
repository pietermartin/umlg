package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.pgm.Edge;

public abstract class ObjectFlowUnknown<T extends ObjectToken<?>> extends ActivityEdge<T> {

	public ObjectFlowUnknown(Edge edge) {
		super(edge);
	}

	@Override
	protected abstract boolean evaluateGuardConditions(T token);

}
