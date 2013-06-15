package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

import java.util.List;

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
