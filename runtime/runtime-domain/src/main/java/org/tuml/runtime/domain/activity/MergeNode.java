package org.tuml.runtime.domain.activity;

import java.util.ArrayList;
import java.util.List;

import com.tinkerpop.blueprints.Vertex;

/*
 * The edges coming into and out of a merge node must be either all object flows or all control flows.
 */
public abstract class MergeNode<IN extends Token, OUT extends Token> extends ControlNode<IN,OUT> {

	public MergeNode() {
		super();
	}
	
	public MergeNode(Vertex vertex) {
		super(vertex);
	}		

	public MergeNode(boolean persist, String name) {
		super(persist, name);
	}
	
	protected abstract ActivityEdge<OUT> getOutFlow();

	@Override
	public List<? extends ActivityEdge<OUT>> getOutgoing() {
		List<ActivityEdge<OUT>> result = new ArrayList<ActivityEdge<OUT>>();
		result.add(getOutFlow());
		return result;
	}	

	@Override
	protected boolean doAllIncomingFlowsHaveTokens() {
		return true;
	}
}
