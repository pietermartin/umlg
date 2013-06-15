package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;
import org.umlg.runtime.domain.activity.interf.IManyInActivityParameterNode;

import java.util.Collections;
import java.util.List;

public abstract class ManyInActivityParameterNode<O> extends InActivityParameterNode<O,CollectionObjectToken<O>> implements IManyInActivityParameterNode<O> {

	public ManyInActivityParameterNode() {
		super();
	}

	public ManyInActivityParameterNode(boolean persist, String name) {
		super(persist, name);
	}

	public ManyInActivityParameterNode(Vertex vertex) {
		super(vertex);
	}

	@Override
	public List<ManyObjectFlowKnown<O>> getIncoming() {
		return Collections.emptyList();
	}

	@Override
	public abstract List<ManyObjectFlowKnown<O>> getOutgoing();

}
