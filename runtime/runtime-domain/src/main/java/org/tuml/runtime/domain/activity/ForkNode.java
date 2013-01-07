package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

import java.util.ArrayList;
import java.util.List;

public abstract class ForkNode<IN extends Token> extends ControlNode<IN,IN> {

	public ForkNode() {
		super();
	}

	public ForkNode(Vertex vertex) {
		super(vertex);
	}

	public ForkNode(boolean persist, String name) {
		super(persist, name);
	}

	protected abstract ActivityEdge<IN> getInFlow();

	@Override
	public List<ActivityEdge<IN>> getIncoming() {
		List<ActivityEdge<IN>> result = new ArrayList<ActivityEdge<IN>>();
		result.add(getInFlow());
		return result;
	}

}
