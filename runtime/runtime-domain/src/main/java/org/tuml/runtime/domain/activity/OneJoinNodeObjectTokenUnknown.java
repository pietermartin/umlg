package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

import java.util.List;

public abstract class OneJoinNodeObjectTokenUnknown extends JoinNodeObjectTokenUnknown<SingleObjectToken<?>> {

	public OneJoinNodeObjectTokenUnknown() {
		super();
	}
	
	public OneJoinNodeObjectTokenUnknown(Vertex vertex) {
		super(vertex);
	}	

	public OneJoinNodeObjectTokenUnknown(boolean persist, String name) {
		super(persist, name);
	}
	
	@Override
	protected abstract OneObjectFlowUnknown getOutFlow();

	@Override
	public abstract List<OneObjectFlowUnknown> getIncoming();
	
}
