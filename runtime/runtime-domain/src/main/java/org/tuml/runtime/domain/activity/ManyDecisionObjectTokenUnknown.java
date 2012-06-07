package org.tuml.runtime.domain.activity;

import java.util.List;

import com.tinkerpop.blueprints.Vertex;

public abstract class ManyDecisionObjectTokenUnknown extends DecisionObjectTokenUnknown<CollectionObjectToken<?>> {

	public ManyDecisionObjectTokenUnknown() {
		super();
	}

	public ManyDecisionObjectTokenUnknown(boolean persist, String name) {
		super(persist, name);
	}

	public ManyDecisionObjectTokenUnknown(Vertex vertex) {
		super(vertex);
	}

	@Override
	protected abstract ManyObjectFlowUnknown getInFlow();

	@Override
	public abstract List<? extends ManyObjectFlowUnknown> getOutgoing();

}
