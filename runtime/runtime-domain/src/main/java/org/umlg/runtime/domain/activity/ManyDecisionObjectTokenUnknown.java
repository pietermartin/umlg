package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

import java.util.List;

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
