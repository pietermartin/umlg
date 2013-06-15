package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

import java.util.List;

public abstract class ManyJoinNodeObjectTokenKnown<O> extends JoinNodeObjectTokenKnown<O, CollectionObjectToken<O>> {

	public ManyJoinNodeObjectTokenKnown() {
		super();
	}

	public ManyJoinNodeObjectTokenKnown(Vertex vertex) {
		super(vertex);
	}

	public ManyJoinNodeObjectTokenKnown(boolean persist, String name) {
		super(persist, name);
	}
	
	@Override
	protected abstract ManyObjectFlowKnown<O> getOutFlow();

	@Override
	public abstract List<ManyObjectFlowKnown<O>> getIncoming();

}
