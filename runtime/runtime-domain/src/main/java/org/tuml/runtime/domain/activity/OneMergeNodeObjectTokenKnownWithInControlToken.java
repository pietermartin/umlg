package org.tuml.runtime.domain.activity;

import java.util.List;

import com.tinkerpop.blueprints.Vertex;


public abstract class OneMergeNodeObjectTokenKnownWithInControlToken<O> extends MergeNodeObjectTokenKnownWithInControlToken<O, SingleObjectToken<O>> {

	public OneMergeNodeObjectTokenKnownWithInControlToken() {
		super();
	}

	public OneMergeNodeObjectTokenKnownWithInControlToken(boolean persist, String name) {
		super(persist, name);
	}

	public OneMergeNodeObjectTokenKnownWithInControlToken(Vertex vertex) {
		super(vertex);
	}
	
	@Override
	protected abstract OneObjectFlowKnown<O> getOutFlow();
	
	@Override
	public abstract List<? extends ActivityEdge<Token>> getIncoming();

}
