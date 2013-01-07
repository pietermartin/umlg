package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

import java.util.ArrayList;
import java.util.List;

public abstract class JoinNodeObjectTokenKnown<O, T extends ObjectToken<O>> extends JoinNode<T, T> {

	public JoinNodeObjectTokenKnown() {
		super();
	}

	public JoinNodeObjectTokenKnown(Vertex vertex) {
		super(vertex);
	}

	public JoinNodeObjectTokenKnown(boolean persist, String name) {
		super(persist, name);
	}

	@Override
	protected abstract ObjectFlowKnown<O, T> getOutFlow();

	@Override
	public abstract List<? extends ObjectFlowKnown<O, T>> getIncoming();

	@Override
	public List<ObjectFlowKnown<O, T>> getOutgoing() {
		List<ObjectFlowKnown<O, T>> result = new ArrayList<ObjectFlowKnown<O, T>>();
		result.add(getOutFlow());
		return result;
	}

}
