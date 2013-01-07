package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

import java.util.List;

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
