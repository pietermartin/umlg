package org.tuml.runtime.domain.activity;

import java.util.List;

import com.tinkerpop.blueprints.Vertex;

public abstract class ManyJoinNodeObjectTokenUnknown extends JoinNodeObjectTokenUnknown<CollectionObjectToken<?>> {

	public ManyJoinNodeObjectTokenUnknown() {
		super();
	}
	
	public ManyJoinNodeObjectTokenUnknown(Vertex vertex) {
		super(vertex);
	}	

	public ManyJoinNodeObjectTokenUnknown(boolean persist, String name) {
		super(persist, name);
	}
	
	@Override
	protected abstract ManyObjectFlowUnknown getOutFlow();

	@Override
	public abstract List<ManyObjectFlowUnknown> getIncoming();
	
}
