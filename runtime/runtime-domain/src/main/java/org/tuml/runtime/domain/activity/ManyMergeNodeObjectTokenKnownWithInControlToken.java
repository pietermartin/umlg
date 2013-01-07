package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

import java.util.List;


public abstract class ManyMergeNodeObjectTokenKnownWithInControlToken<O> extends MergeNodeObjectTokenKnownWithInControlToken<O, CollectionObjectToken<O>> {

	public ManyMergeNodeObjectTokenKnownWithInControlToken() {
		super();
	}

	public ManyMergeNodeObjectTokenKnownWithInControlToken(boolean persist, String name) {
		super(persist, name);
	}

	public ManyMergeNodeObjectTokenKnownWithInControlToken(Vertex vertex) {
		super(vertex);
	}
	
	@Override
	protected abstract ManyObjectFlowKnown<O> getOutFlow();
	
	@Override
	public abstract List<? extends ActivityEdge<Token>> getIncoming();

}
