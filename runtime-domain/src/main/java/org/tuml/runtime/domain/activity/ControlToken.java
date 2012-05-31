package org.tuml.runtime.domain.activity;

import org.tuml.runtime.adaptor.GraphDb;

import com.tinkerpop.blueprints.pgm.Vertex;

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
		if (!node.vertex.getOutEdges(TOKEN + getEdgeName()).iterator().hasNext()) {
			GraphDb.getDb().addEdge(null, node.vertex, getVertex(), TOKEN + getEdgeName());
		} else {
			// TODO write test case for this
			GraphDb.getDb().removeVertex(getVertex());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ControlToken duplicate(String flowName) {
		return new ControlToken(flowName);
	}

	@Override
	public void remove() {
		GraphDb.getDb().removeVertex(getVertex());
	}

}
