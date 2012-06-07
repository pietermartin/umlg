package org.tuml.runtime.domain.activity;

import java.util.List;

import com.tinkerpop.blueprints.Vertex;

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
