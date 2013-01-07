package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

import java.util.List;

public abstract class OneJoinNodeObjectTokenKnown<O> extends JoinNodeObjectTokenKnown<O, SingleObjectToken<O>> {

	public OneJoinNodeObjectTokenKnown() {
		super();
	}

	public OneJoinNodeObjectTokenKnown(Vertex vertex) {
		super(vertex);
	}

	public OneJoinNodeObjectTokenKnown(boolean persist, String name) {
		super(persist, name);
	}

	@Override
	protected abstract OneObjectFlowKnown<O> getOutFlow();

	@Override
	public abstract List<OneObjectFlowKnown<O>> getIncoming();

}
