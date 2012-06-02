package org.tuml.runtime.domain.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.tuml.runtime.domain.activity.interf.IInActivityParameterNode;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class InActivityParameterNode<O, IN extends ObjectToken<O>> extends ActivityParameterNode<O, IN> implements IInActivityParameterNode<O, IN> {

	private static final long serialVersionUID = 8152193782924762412L;

	public InActivityParameterNode() {
		super();
	}

	public InActivityParameterNode(boolean persist, String name) {
		super(persist, name);
	}

	public InActivityParameterNode(Vertex vertex) {
		super(vertex);
	}
	
	@Override
	protected Boolean executeNode() {
		List<Boolean> flowResult = new ArrayList<Boolean>();

		setNodeStatus(NodeStatus.ENABLED);
		setNodeStatus(NodeStatus.ACTIVE);

		this.nodeStat.increment();

		for (IN objectToken : getInTokens()) {
			// For each out flow add a token
			for (ObjectFlowKnown<O, IN> flow : getOutgoing()) {
				IN duplicate = objectToken.duplicate(flow.getName());
				addOutgoingToken(duplicate);
			}
			objectToken.remove();
		}
		// Continue each out flow with its tokens
		for (ObjectFlowKnown<O,IN> flow : getOutgoing()) {
			flow.setStarts(getOutTokens(flow.getName()));
			flowResult.add(flow.processNextStart());
		}

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
	
	@Override
	public List<? extends ObjectFlowKnown<O, IN>> getIncoming() {
		return Collections.emptyList();
	}
	
	@Override
	public List<IN> getInTokens() {
		List<IN> result = new ArrayList<IN>();
		Iterable<Edge> iter = this.vertex.getOutEdges(Token.TOKEN + getName());
		for (Edge edge : iter) {
			result.add(constructInToken(edge));
		}
		return result;
	}	
	
}
