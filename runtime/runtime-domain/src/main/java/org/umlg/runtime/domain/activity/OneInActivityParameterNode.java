package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;
import org.umlg.runtime.domain.activity.interf.IOneInActivityParameterNode;

import java.util.Collections;
import java.util.List;

public abstract class OneInActivityParameterNode<O> extends InActivityParameterNode<O,SingleObjectToken<O>> implements IOneInActivityParameterNode<O> {

	public OneInActivityParameterNode() {
		super();
	}

	public OneInActivityParameterNode(boolean persist, String name) {
		super(persist, name);
	}

	public OneInActivityParameterNode(Vertex vertex) {
		super(vertex);
	}
	
	@Override
	public List<OneObjectFlowKnown<O>> getIncoming() {
		return Collections.emptyList();
	}

	@Override
	public abstract List<OneObjectFlowKnown<O>> getOutgoing();

}
