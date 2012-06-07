package org.tuml.runtime.domain.activity;

import java.util.List;

import com.tinkerpop.blueprints.Vertex;

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
