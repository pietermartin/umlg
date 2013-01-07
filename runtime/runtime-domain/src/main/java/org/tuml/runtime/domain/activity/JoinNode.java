package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

import java.util.ArrayList;
import java.util.List;

public abstract class JoinNode<IN extends Token, OUT extends Token> extends ControlNode<IN, OUT> {

	public JoinNode() {
		super();
	}

	public JoinNode(Vertex vertex) {
		super(vertex);
	}

	public JoinNode(boolean persist, String name) {
		super(persist, name);
	}

	@Override
	public abstract List<? extends ActivityEdge<? extends IN>> getIncoming();

	protected abstract ActivityEdge<OUT> getOutFlow();

	@Override
	public List<? extends ActivityEdge<OUT>> getOutgoing() {
		List<ActivityEdge<OUT>> result = new ArrayList<ActivityEdge<OUT>>();
		result.add(getOutFlow());
		return result;
	}

}
