package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

import java.util.ArrayList;
import java.util.List;

public abstract class JoinNodeControlToken extends JoinNode<ControlToken, ControlToken> {

	public JoinNodeControlToken() {
		super();
	}
	
	public JoinNodeControlToken(Vertex vertex) {
		super(vertex);
	}	

	public JoinNodeControlToken(boolean persist, String name) {
		super(persist, name);
	}
	
	@Override
	protected abstract ControlFlow getOutFlow();

	@Override
	public List<ControlFlow> getOutgoing() {
		List<ControlFlow> result = new ArrayList<ControlFlow>();
		result.add(getOutFlow());
		return result;
	}
	
	@Override
	public abstract List<ControlFlow> getIncoming();	

	@Override
	public List<ControlToken> getInTokens() {
		List<ControlToken> result = new ArrayList<ControlToken>();
		for (ControlFlow flow : getIncoming()) {
			Iterable<Edge> iter = this.vertex.getEdges(Direction.OUT, Token.TOKEN + flow.getName());
			for (Edge edge : iter) {
				result.add(new ControlToken(edge.getVertex(Direction.IN)));
			}
		}
		return result;
	}

	@Override
	public List<ControlToken> getInTokens(String inFlowName) {
		List<ControlToken> result = new ArrayList<ControlToken>();
		for (ControlFlow flow : getIncoming()) {
			if (flow.getName().equals(inFlowName)) {
				Iterable<Edge> iter = this.vertex.getEdges(Direction.OUT, Token.TOKEN + flow.getName());
				for (Edge edge : iter) {
					result.add(new ControlToken(edge.getVertex(Direction.IN)));
				}
			}
		}
		return result;
	}

	@Override
	public List<ControlToken> getOutTokens() {
		List<ControlToken> result = new ArrayList<ControlToken>();
		for (ControlFlow flow : getOutgoing()) {
			Iterable<Edge> iter = this.vertex.getEdges(Direction.OUT, Token.TOKEN + flow.getName());
			for (Edge edge : iter) {
				result.add(new ControlToken(edge.getVertex(Direction.IN)));
			}
		}
		return result;
	}

	@Override
	public List<ControlToken> getOutTokens(String outFlowName) {
		List<ControlToken> result = new ArrayList<ControlToken>();
		for (ControlFlow flow : getOutgoing()) {
			if (flow.getName().equals(outFlowName)) {
				Iterable<Edge> iter = this.vertex.getEdges(Direction.OUT, Token.TOKEN + flow.getName());
				for (Edge edge : iter) {
					result.add(new ControlToken(edge.getVertex(Direction.IN)));
				}
			}
		}
		return result;
	}

}
