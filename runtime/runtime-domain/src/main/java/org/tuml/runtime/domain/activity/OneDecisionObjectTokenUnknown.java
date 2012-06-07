package org.tuml.runtime.domain.activity;

import java.util.List;

import com.tinkerpop.blueprints.Vertex;

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
