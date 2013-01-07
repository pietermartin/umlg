package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

import java.util.List;

public abstract class OneDecisionObjectTokenUnknown extends DecisionObjectTokenUnknown<SingleObjectToken<?>> {

	public OneDecisionObjectTokenUnknown() {
		super();
	}

	public OneDecisionObjectTokenUnknown(boolean persist, String name) {
		super(persist, name);
	}

	public OneDecisionObjectTokenUnknown(Vertex vertex) {
		super(vertex);
	}

	@Override
	protected abstract OneObjectFlowUnknown getInFlow();

	@Override
	public abstract List<? extends OneObjectFlowUnknown> getOutgoing();

}
