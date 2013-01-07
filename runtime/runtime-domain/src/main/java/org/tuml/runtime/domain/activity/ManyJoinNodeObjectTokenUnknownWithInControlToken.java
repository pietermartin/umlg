package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

import java.util.List;

public abstract class ManyJoinNodeObjectTokenUnknownWithInControlToken extends JoinNodeObjectTokenUnknownWithInControlToken<CollectionObjectToken<?>> {

	public ManyJoinNodeObjectTokenUnknownWithInControlToken() {
		super();
	}

	public ManyJoinNodeObjectTokenUnknownWithInControlToken(Vertex vertex) {
		super(vertex);
	}

	public ManyJoinNodeObjectTokenUnknownWithInControlToken(boolean persist, String name) {
		super(persist, name);
	}

	@Override
	protected abstract ManyObjectFlowUnknown getOutFlow();


	@Override
	public abstract List<ActivityEdge<Token>> getIncoming();

}
