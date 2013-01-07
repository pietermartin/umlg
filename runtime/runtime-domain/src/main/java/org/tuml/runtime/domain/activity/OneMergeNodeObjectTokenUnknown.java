package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

import java.util.List;


public abstract class OneMergeNodeObjectTokenUnknown extends MergeNodeObjectTokenUnknown<SingleObjectToken<?>,SingleObjectToken<?>> {

	public OneMergeNodeObjectTokenUnknown() {
		super();
	}

	public OneMergeNodeObjectTokenUnknown(boolean persist, String name) {
		super(persist, name);
	}

	public OneMergeNodeObjectTokenUnknown(Vertex vertex) {
		super(vertex);
	}
	
	@Override
	protected abstract OneObjectFlowUnknown getOutFlow();

	@Override
	public abstract List<? extends OneObjectFlowUnknown> getIncoming();

}
