package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

import java.util.ArrayList;
import java.util.List;

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
