package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

import java.util.List;


public abstract class MergeNodeObjectTokenUnknownWithInControlToken<OUT extends ObjectToken<?>> extends MergeNode<Token, OUT> {

	public MergeNodeObjectTokenUnknownWithInControlToken() {
		super();
	}

	public MergeNodeObjectTokenUnknownWithInControlToken(boolean persist, String name) {
		super(persist, name);
	}

	public MergeNodeObjectTokenUnknownWithInControlToken(Vertex vertex) {
		super(vertex);
	}
	
	@Override
	protected abstract ObjectFlowUnknown<OUT> getOutFlow();
	
	@Override
	public abstract List<? extends ActivityEdge<Token>> getIncoming();

}
