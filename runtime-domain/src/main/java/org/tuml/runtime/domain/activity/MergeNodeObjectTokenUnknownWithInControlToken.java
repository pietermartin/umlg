package org.tuml.runtime.domain.activity;

import java.util.List;

import com.tinkerpop.blueprints.pgm.Vertex;


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
