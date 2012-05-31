package org.tuml.runtime.domain.activity;

import java.util.ArrayList;
import java.util.List;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class DecisionControlToken extends DecisionNode<ControlToken> {

	public DecisionControlToken() {
		super();
	}

	public DecisionControlToken(boolean persist, String name) {
		super(persist, name);
	}

	public DecisionControlToken(Vertex vertex) {
		super(vertex);
	}

	public List<ControlToken> getInTokens() {
		List<ControlToken> result = new ArrayList<ControlToken>();
		for (ActivityEdge<ControlToken> flow : getIncoming()) {
			Iterable<Edge> iter = this.vertex.getOutEdges(Token.TOKEN + flow.getName());
			for (Edge edge : iter) {
				result.add(new ControlToken(edge.getInVertex()));
			}
		}
		return result;
	}

	public List<ControlToken> getInTokens(String inFlowName) {
		List<ControlToken> result = new ArrayList<ControlToken>();
		for (ActivityEdge<ControlToken> flow : getIncoming()) {
			if (flow.getName().equals(inFlowName)) {
				Iterable<Edge> iter = this.vertex.getOutEdges(Token.TOKEN + flow.getName());
				for (Edge edge : iter) {
					result.add(new ControlToken(edge.getInVertex()));
				}
			}
		}
		return result;
	}

	public List<ControlToken> getOutTokens() {
		List<ControlToken> result = new ArrayList<ControlToken>();
		for (ActivityEdge<ControlToken> flow : getOutgoing()) {
			Iterable<Edge> iter = this.vertex.getOutEdges(Token.TOKEN + flow.getName());
			for (Edge edge : iter) {
				result.add(new ControlToken(edge.getInVertex()));
			}
		}
		return result;
	}

	public List<ControlToken> getOutTokens(String outFlowName) {
		List<ControlToken> result = new ArrayList<ControlToken>();
		for (ActivityEdge<ControlToken> flow : getOutgoing()) {
			if (flow.getName().equals(outFlowName)) {
				Iterable<Edge> iter = this.vertex.getOutEdges(Token.TOKEN + flow.getName());
				for (Edge edge : iter) {
					result.add(new ControlToken(edge.getInVertex()));
				}
			}
		}
		return result;
	}

}
