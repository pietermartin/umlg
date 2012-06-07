package org.tuml.runtime.domain.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.tuml.runtime.domain.activity.interf.IManyOutActivityParameterNode;

import com.tinkerpop.blueprints.Vertex;

public abstract class ManyOutActivityParameterNode<O> extends OutActivityParameterNode<O,CollectionObjectToken<O>> implements IManyOutActivityParameterNode<O> {

	private static final long serialVersionUID = 631187214474235L;

	public ManyOutActivityParameterNode() {
		super();
	}

	public ManyOutActivityParameterNode(boolean persist, String name) {
		super(persist, name);
	}

	public ManyOutActivityParameterNode(Vertex vertex) {
		super(vertex);
	}
	
	@Override
	public abstract List<ManyObjectFlowKnown<O>> getIncoming();

	@Override
	public List<ManyObjectFlowKnown<O>> getOutgoing() {
		return Collections.emptyList();
	}

	@Override
	public List<O> getReturnParameterValues() {
		List<O> result = new ArrayList<O>();
		for (CollectionObjectToken<O> token : getInTokens()) {
			result.addAll(token.getElements());
		}
		return result;
	}

}
