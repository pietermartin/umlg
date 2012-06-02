package org.tuml.runtime.domain.activity;

import java.util.List;

import com.tinkerpop.blueprints.pgm.Vertex;

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
