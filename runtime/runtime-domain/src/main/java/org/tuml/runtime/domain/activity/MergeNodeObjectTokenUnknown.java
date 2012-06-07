package org.tuml.runtime.domain.activity;

import java.util.List;

import com.tinkerpop.blueprints.Vertex;


public abstract class MergeNodeObjectTokenUnknown<IN extends ObjectToken<?>, OUT extends ObjectToken<?>> extends MergeNode<IN, OUT> {

	public MergeNodeObjectTokenUnknown() {
		super();
	}

	public MergeNodeObjectTokenUnknown(boolean persist, String name) {
		super(persist, name);
	}

	public MergeNodeObjectTokenUnknown(Vertex vertex) {
		super(vertex);
	}
	
	@Override
	protected abstract ObjectFlowUnknown<OUT> getOutFlow();

	@Override
	public abstract List<? extends ObjectFlowUnknown<IN>> getIncoming();

}
