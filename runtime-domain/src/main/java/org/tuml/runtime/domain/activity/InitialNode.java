package org.tuml.runtime.domain.activity;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;


public abstract class InitialNode extends ControlNode<ControlToken, ControlToken> {

	public InitialNode() {
		super();
	}
	
	public InitialNode(Vertex vertex) {
		super(vertex);
	}	

	public InitialNode(boolean persist, String name) {
		super(persist, name);
	}

	protected boolean doAllIncomingFlowsHaveTokens() {
		return true;
	}
	
	@Override
	public List<? extends ActivityEdge<ControlToken>> getIncoming() {
		return Collections.<ActivityEdge<ControlToken>>emptyList();
	}

	@Override
	public boolean mayContinue() {
		return true;
	}
	
	@Override
	public List<ControlToken> getInTokens() {
		List<ControlToken> result = new ArrayList<ControlToken>();
		Iterable<Edge> iter = this.vertex.getOutEdges(Token.TOKEN + getName());
		for (Edge edge : iter) {
			result.add(new ControlToken(edge.getInVertex()));
		}
		return result;
	}

	@Override
	public List<ControlToken> getInTokens(String inFlowName) {
		throw new IllegalStateException("Initial nodes do not have in flows");
	}

	@Override
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

	@Override
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
