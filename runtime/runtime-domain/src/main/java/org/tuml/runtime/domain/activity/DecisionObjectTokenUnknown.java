package org.tuml.runtime.domain.activity;

import java.util.ArrayList;
import java.util.List;

import com.tinkerpop.blueprints.Vertex;

public abstract class DecisionObjectTokenUnknown<IN extends ObjectToken<?>> extends DecisionNode<IN> {

	public DecisionObjectTokenUnknown() {
		super();
	}

	public DecisionObjectTokenUnknown(boolean persist, String name) {
		super(persist, name);
	}

	public DecisionObjectTokenUnknown(Vertex vertex) {
		super(vertex);
	}

	@Override
	protected abstract ObjectFlowUnknown<IN> getInFlow();

	@Override
	public abstract List<? extends ObjectFlowUnknown<IN>> getOutgoing();

	@Override
	public List<ObjectFlowUnknown<IN>> getIncoming() {
		List<ObjectFlowUnknown<IN>> result = new ArrayList<ObjectFlowUnknown<IN>>();
		result.add(getInFlow());
		return result;
	}

}
