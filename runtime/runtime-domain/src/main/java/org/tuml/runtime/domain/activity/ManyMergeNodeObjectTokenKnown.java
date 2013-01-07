package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

import java.util.List;

public abstract class ManyMergeNodeObjectTokenKnown<O> extends MergeNodeObjectTokenKnown<O, CollectionObjectToken<O>, CollectionObjectToken<O>> {

	public ManyMergeNodeObjectTokenKnown() {
		super();
	}

	public ManyMergeNodeObjectTokenKnown(boolean persist, String name) {
		super(persist, name);
	}

	public ManyMergeNodeObjectTokenKnown(Vertex vertex) {
		super(vertex);
	}

	@Override
	protected abstract ManyObjectFlowKnown<O> getOutFlow();

	@Override
	public abstract List<? extends ManyObjectFlowKnown<O>> getIncoming();

}
