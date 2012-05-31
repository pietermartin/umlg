package org.tuml.runtime.domain.activity;

import java.util.List;

import com.tinkerpop.blueprints.pgm.Vertex;

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
