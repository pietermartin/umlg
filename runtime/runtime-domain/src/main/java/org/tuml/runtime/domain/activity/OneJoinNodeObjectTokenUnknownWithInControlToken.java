package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

import java.util.List;

public abstract class OneJoinNodeObjectTokenUnknownWithInControlToken extends JoinNodeObjectTokenUnknownWithInControlToken<SingleObjectToken<?>> {

	public OneJoinNodeObjectTokenUnknownWithInControlToken() {
		super();
	}

	public OneJoinNodeObjectTokenUnknownWithInControlToken(Vertex vertex) {
		super(vertex);
	}

	public OneJoinNodeObjectTokenUnknownWithInControlToken(boolean persist, String name) {
		super(persist, name);
	}

	@Override
	protected abstract OneObjectFlowUnknown getOutFlow();

	@Override
	public abstract List<? extends ActivityEdge<? extends Token>> getIncoming();

}
