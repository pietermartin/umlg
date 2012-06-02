package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.pgm.Edge;

public abstract class ObjectFlowKnown<O, T extends ObjectToken<O>> extends ActivityEdge<T> {

	public ObjectFlowKnown(Edge edge) {
		super(edge);
	}
	
	protected Edge getEdge() {
		return this.edge;
	}
	
	@Override
	protected abstract boolean evaluateGuardConditions(T token);
	
}
