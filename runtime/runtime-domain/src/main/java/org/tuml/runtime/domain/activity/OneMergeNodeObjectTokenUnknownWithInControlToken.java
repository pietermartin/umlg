package org.tuml.runtime.domain.activity;

import java.util.List;

import com.tinkerpop.blueprints.pgm.Vertex;


public abstract class OneMergeNodeObjectTokenUnknownWithInControlToken extends MergeNodeObjectTokenUnknownWithInControlToken<SingleObjectToken<?>> {

	public OneMergeNodeObjectTokenUnknownWithInControlToken() {
		super();
	}

	public OneMergeNodeObjectTokenUnknownWithInControlToken(boolean persist, String name) {
		super(persist, name);
	}

	public OneMergeNodeObjectTokenUnknownWithInControlToken(Vertex vertex) {
		super(vertex);
	}
	
	@Override
	protected abstract OneObjectFlowUnknown getOutFlow();
	
	@Override
	public abstract List<? extends ActivityEdge<Token>> getIncoming();

}
