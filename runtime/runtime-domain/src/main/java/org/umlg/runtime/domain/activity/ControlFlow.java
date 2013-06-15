package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Edge;

public abstract class ControlFlow extends ActivityEdge<ControlToken> {

	public ControlFlow(Edge edge) {
		super(edge);
	}

	protected abstract boolean evaluateGuardConditions();

	@Override
	protected boolean evaluateGuardConditions(ControlToken token) {
		return evaluateGuardConditions();	
	}	
}
