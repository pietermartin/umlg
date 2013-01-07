package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

import java.util.List;


public abstract class ManyMergeNodeObjectTokenUnknownWithInControlToken extends MergeNodeObjectTokenUnknownWithInControlToken<CollectionObjectToken<?>> {

	public ManyMergeNodeObjectTokenUnknownWithInControlToken() {
		super();
	}

	public ManyMergeNodeObjectTokenUnknownWithInControlToken(boolean persist, String name) {
		super(persist, name);
	}

	public ManyMergeNodeObjectTokenUnknownWithInControlToken(Vertex vertex) {
		super(vertex);
	}
	
	@Override
	protected abstract ManyObjectFlowUnknown getOutFlow();
	
	@Override
	public abstract List<? extends ActivityEdge<Token>> getIncoming();

}
