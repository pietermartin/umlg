package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

import java.util.List;


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
