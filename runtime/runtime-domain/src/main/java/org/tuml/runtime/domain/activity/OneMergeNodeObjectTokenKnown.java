package org.tuml.runtime.domain.activity;

import java.util.List;

import com.tinkerpop.blueprints.Vertex;

public abstract class OneMergeNodeObjectTokenKnown<O> extends MergeNodeObjectTokenKnown<O, SingleObjectToken<O>, SingleObjectToken<O>> {

	public OneMergeNodeObjectTokenKnown() {
		super();
	}

	public OneMergeNodeObjectTokenKnown(boolean persist, String name) {
		super(persist, name);
	}

	public OneMergeNodeObjectTokenKnown(Vertex vertex) {
		super(vertex);
	}

	@Override
	protected abstract OneObjectFlowKnown<O> getOutFlow();

	@Override
	public abstract List<? extends OneObjectFlowKnown<O>> getIncoming();

}
