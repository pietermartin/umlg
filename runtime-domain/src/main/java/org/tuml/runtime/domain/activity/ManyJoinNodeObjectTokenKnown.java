package org.tuml.runtime.domain.activity;

import java.util.List;

import com.tinkerpop.blueprints.pgm.Vertex;

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
