package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;
import org.tuml.runtime.domain.activity.interf.IOutActivityParameterNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class OutActivityParameterNode<O, IN extends ObjectToken<O>> extends ActivityParameterNode<O, IN> implements IOutActivityParameterNode<O, IN> {

	private static final long serialVersionUID = 1874653435184494635L;

	public OutActivityParameterNode() {
		super();
	}

	public OutActivityParameterNode(boolean persist, String name) {
		super(persist, name);
	}

	public OutActivityParameterNode(Vertex vertex) {
		super(vertex);
	}

	@Override
	public List<? extends ObjectFlowKnown<O, IN>> getOutgoing() {
		return Collections.emptyList();
	}
	
	@Override
	protected Boolean executeNode() {
		List<Boolean> flowResult = new ArrayList<Boolean>();

		setNodeStatus(NodeStatus.ENABLED);
		setNodeStatus(NodeStatus.ACTIVE);

		this.nodeStat.increment();

		setNodeStatus(NodeStatus.COMPLETE);
		boolean result = true;
		for (Boolean b : flowResult) {
			if (!b) {
				result = false;
				break;
			}
		}
		return result;
	}

	public abstract List<O> getReturnParameterValues();
}
