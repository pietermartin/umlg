package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

import java.util.List;


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
