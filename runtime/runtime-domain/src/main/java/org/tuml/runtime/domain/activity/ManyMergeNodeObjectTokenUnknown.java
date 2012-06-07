package org.tuml.runtime.domain.activity;

import java.util.List;

import com.tinkerpop.blueprints.Vertex;


public abstract class ManyMergeNodeObjectTokenUnknown extends MergeNodeObjectTokenUnknown<CollectionObjectToken<?>, CollectionObjectToken<?>> {

	public ManyMergeNodeObjectTokenUnknown() {
		super();
	}

	public ManyMergeNodeObjectTokenUnknown(boolean persist, String name) {
		super(persist, name);
	}

	public ManyMergeNodeObjectTokenUnknown(Vertex vertex) {
		super(vertex);
	}
	
	@Override
	protected abstract ManyObjectFlowUnknown getOutFlow();

	@Override
	public abstract List<? extends ManyObjectFlowUnknown> getIncoming();

}
