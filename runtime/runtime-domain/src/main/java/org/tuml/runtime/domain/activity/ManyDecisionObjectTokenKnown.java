package org.tuml.runtime.domain.activity;

import java.util.List;

import com.tinkerpop.blueprints.Vertex;

public abstract class ManyDecisionObjectTokenKnown<O> extends DecisionObjectTokenKnown<O, CollectionObjectToken<O>> {

	public ManyDecisionObjectTokenKnown() {
		super();
	}

	public ManyDecisionObjectTokenKnown(boolean persist, String name) {
		super(persist, name);
	}

	public ManyDecisionObjectTokenKnown(Vertex vertex) {
		super(vertex);
	}
	
	@Override
	protected abstract ManyObjectFlowKnown<O> getInFlow();

	@Override
	public abstract List<? extends ManyObjectFlowKnown<O>> getOutgoing();

}
