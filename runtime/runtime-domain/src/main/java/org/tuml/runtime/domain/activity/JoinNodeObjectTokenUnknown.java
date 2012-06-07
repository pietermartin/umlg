package org.tuml.runtime.domain.activity;

import java.util.ArrayList;
import java.util.List;

import com.tinkerpop.blueprints.Vertex;

public abstract class JoinNodeObjectTokenUnknown<IN extends ObjectToken<?>> extends JoinNode<IN, IN> {

	public JoinNodeObjectTokenUnknown() {
		super();
	}
	
	public JoinNodeObjectTokenUnknown(Vertex vertex) {
		super(vertex);
	}	

	public JoinNodeObjectTokenUnknown(boolean persist, String name) {
		super(persist, name);
	}
	
	@Override
	protected abstract ObjectFlowUnknown<IN> getOutFlow();

	@Override
	public abstract List<? extends ObjectFlowUnknown<IN>> getIncoming();

	@Override
	public List<ObjectFlowUnknown<IN>> getOutgoing() {
		List<ObjectFlowUnknown<IN>> result = new ArrayList<ObjectFlowUnknown<IN>>();
		result.add(getOutFlow());
		return result;
	}
	
}
