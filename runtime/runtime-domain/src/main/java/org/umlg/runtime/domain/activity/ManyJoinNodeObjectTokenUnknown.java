package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

import java.util.List;

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
