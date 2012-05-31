package org.tuml.runtime.domain.activity;

import java.util.ArrayList;
import java.util.List;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;

/*
 * Join node can have one in control flow and one in object flow. In this case an object flow flows out
 */
public abstract class JoinNodeObjectTokenKnownWithInControlToken<O, OUT extends ObjectToken<O>> extends JoinNode<Token, OUT> {

	public JoinNodeObjectTokenKnownWithInControlToken() {
		super();
	}

	public JoinNodeObjectTokenKnownWithInControlToken(Vertex vertex) {
		super(vertex);
	}

	public JoinNodeObjectTokenKnownWithInControlToken(boolean persist, String name) {
		super(persist, name);
	}

	@Override
	protected abstract ObjectFlowKnown<O,OUT> getOutFlow();

	@Override
	public abstract List<ActivityEdge<Token>> getIncoming();

	//TODO
	/*
	 * (non-Javadoc)
	 * @see org.nakeduml.runtime.domain.activity.JoinNode#getInTokens()
	 * 
	 * Consume control tokens, only object token continue.
	 */
	@Override
	public List<Token> getInTokens() {
		if (true) {
			throw new IllegalStateException("Checka this out");
			//Not sure about this consuming, consume elsewhere
		}
		List<Token> result = new ArrayList<Token>();
		for (ActivityEdge<? extends Token> flow : getIncoming()) {
			Iterable<Edge> iter = this.vertex.getOutEdges(Token.TOKEN + flow.getName());
			for (Edge edge : iter) {
				Token token;
				if (!(flow instanceof ControlFlow)) {
					token = contructOutToken(edge);
					result.add(token);
				} else {
					token = new ControlToken(edge.getInVertex());
					token.remove();
				}
			}
		}
		return result;
	}

}
