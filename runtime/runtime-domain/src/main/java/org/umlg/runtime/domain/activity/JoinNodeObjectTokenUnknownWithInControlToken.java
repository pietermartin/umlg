package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

import java.util.ArrayList;
import java.util.List;

public abstract class JoinNodeObjectTokenUnknownWithInControlToken<OUT extends ObjectToken<?>> extends JoinNode<Token, OUT> {

	public JoinNodeObjectTokenUnknownWithInControlToken() {
		super();
	}

	public JoinNodeObjectTokenUnknownWithInControlToken(Vertex vertex) {
		super(vertex);
	}

	public JoinNodeObjectTokenUnknownWithInControlToken(boolean persist, String name) {
		super(persist, name);
	}

	@Override
	protected abstract ObjectFlowUnknown<OUT> getOutFlow();

	@Override
	public List<ObjectFlowUnknown<OUT>> getOutgoing() {
		List<ObjectFlowUnknown<OUT>> result = new ArrayList<ObjectFlowUnknown<OUT>>();
		result.add(getOutFlow());
		return result;
	}

	@Override
	public abstract List<? extends ActivityEdge<? extends Token>> getIncoming();

	/*
	 * (non-Javadoc)
	 * @see org.nakeduml.runtime.domain.activity.JoinNode#getInTokens()
	 * 
	 * Consume control tokens, only object token continue.
	 */
	@Override
	public List<Token> getInTokens() {
		List<Token> result = new ArrayList<Token>();
		for (ActivityEdge<? extends Token> flow : getIncoming()) {
			Iterable<Edge> iter = this.vertex.getEdges(Direction.OUT, Token.TOKEN + flow.getName());
			for (Edge edge : iter) {
				Token token;
				if (!(flow instanceof ControlFlow)) {
					token = contructOutToken(edge);
					result.add(token);
				} else {
					token = new ControlToken(edge.getVertex(Direction.IN));
					token.remove();
				}
			}
		}
		return result;
	}

}
