package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import org.umlg.runtime.adaptor.UMLG;

public class ControlToken extends Token {

	public ControlToken(Vertex vertex) {
		super(vertex);
	}

	public ControlToken(String edgeName) {
		super(edgeName);
	}

	@Override
	protected void addEdgeToActivityNode(ActivityNode<? extends Token, ? extends Token> node) {
		// Multiple tokens from the same incoming edge is merged
		if (!node.vertex.getEdges(Direction.OUT, TOKEN + getEdgeName()).iterator().hasNext()) {
			UMLG.get().addEdge(null, node.vertex, getVertex(), TOKEN + getEdgeName());
		} else {
			// TODO write test case for this
			UMLG.get().removeVertex(getVertex());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ControlToken duplicate(String flowName) {
		return new ControlToken(flowName);
	}

	@Override
	public void remove() {
		UMLG.get().removeVertex(getVertex());
	}

}
