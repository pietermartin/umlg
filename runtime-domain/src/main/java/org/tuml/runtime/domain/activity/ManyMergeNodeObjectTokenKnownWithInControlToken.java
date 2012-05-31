package org.tuml.runtime.domain.activity;

import java.util.List;

import com.tinkerpop.blueprints.pgm.Vertex;


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
