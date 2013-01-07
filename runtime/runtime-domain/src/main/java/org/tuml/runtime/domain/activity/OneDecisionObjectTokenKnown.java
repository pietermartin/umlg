package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

import java.util.List;

public abstract class OneDecisionObjectTokenKnown<O> extends DecisionObjectTokenKnown<O, SingleObjectToken<O>> {

	public OneDecisionObjectTokenKnown() {
		super();
	}

	public OneDecisionObjectTokenKnown(boolean persist, String name) {
		super(persist, name);
	}

	public OneDecisionObjectTokenKnown(Vertex vertex) {
		super(vertex);
	}
	
	@Override
	protected abstract OneObjectFlowKnown<O> getInFlow();

	@Override
	public abstract List<? extends OneObjectFlowKnown<O>> getOutgoing();

}
