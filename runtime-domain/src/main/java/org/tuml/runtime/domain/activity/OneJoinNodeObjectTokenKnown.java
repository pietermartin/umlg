package org.tuml.runtime.domain.activity;

import java.util.List;

import com.tinkerpop.blueprints.pgm.Vertex;

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
