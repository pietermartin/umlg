package org.tuml.runtime.domain.activity;

import java.util.List;

import com.tinkerpop.blueprints.pgm.Vertex;

/*
 * Join node can have one in control flow and one in object flow. In this case an object flow flows out
 */
public abstract class OneJoinNodeObjectTokenKnownWithInControlToken<O> extends JoinNodeObjectTokenKnownWithInControlToken<O, SingleObjectToken<O>> {

	public OneJoinNodeObjectTokenKnownWithInControlToken() {
		super();
	}

	public OneJoinNodeObjectTokenKnownWithInControlToken(Vertex vertex) {
		super(vertex);
	}

	public OneJoinNodeObjectTokenKnownWithInControlToken(boolean persist, String name) {
		super(persist, name);
	}
	
	@Override
	protected abstract OneObjectFlowKnown<O> getOutFlow();

	@Override
	public abstract List<ActivityEdge<Token>> getIncoming();

}
