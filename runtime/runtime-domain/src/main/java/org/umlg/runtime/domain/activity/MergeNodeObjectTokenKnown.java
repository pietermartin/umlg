package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

import java.util.List;

public abstract class MergeNodeObjectTokenKnown<O,IN extends ObjectToken<O>,OUT extends ObjectToken<O>> extends MergeNode<IN, OUT> {

	public MergeNodeObjectTokenKnown() {
		super();
	}

	public MergeNodeObjectTokenKnown(boolean persist, String name) {
		super(persist, name);
	}

	public MergeNodeObjectTokenKnown(Vertex vertex) {
		super(vertex);
	}

	@Override
	protected abstract ObjectFlowKnown<O,OUT> getOutFlow();

	@Override
	public abstract List<? extends ObjectFlowKnown<O,IN>> getIncoming();

}
