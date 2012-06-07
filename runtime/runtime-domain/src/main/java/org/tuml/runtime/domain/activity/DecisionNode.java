package org.tuml.runtime.domain.activity;

import java.util.ArrayList;
import java.util.List;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.pipes.util.iterators.SingleIterator;

/*
 * The edges coming into and out of a decision node, other than the decision input flow (if any), must be either all 
 * object flows or all control flows.
 */
public abstract class DecisionNode<IN  extends Token> extends ControlNode<IN, IN> {

	public DecisionNode() {
		super();
	}
	
	public DecisionNode(Vertex vertex) {
		super(vertex);
	}	

	public DecisionNode(boolean persist, String name) {
		super(persist, name);
	}

	@Override
	public abstract List<? extends ActivityEdge<IN>> getOutgoing();

	protected abstract ActivityEdge<IN> getInFlow();

	@Override
	public List<? extends ActivityEdge<IN>> getIncoming() {
		List<ActivityEdge<IN>> result = new ArrayList<ActivityEdge<IN>>();
		result.add(getInFlow());
		return result;
	}
	
	@Override
	protected Boolean executeNode() {
		List<Boolean> flowResult = new ArrayList<Boolean>();

		setNodeStatus(NodeStatus.ENABLED);
		setNodeStatus(NodeStatus.ACTIVE);

		this.nodeStat.increment();

		boolean oneOutgoingFlowGuardSucceeded = false;
		for (IN token : getInTokens()) {
			// For each out flow add a token
			for (ActivityEdge<IN> flow : getOutgoing()) {
				if (flow.evaluateGuardConditions(token)) {
					oneOutgoingFlowGuardSucceeded = true;
					IN duplicate = token.duplicate(flow.getName());
					addOutgoingToken(duplicate);
					flow.setStarts(new SingleIterator<IN>(duplicate));
					// Continue each out flow with its tokens
					flowResult.add(flow.processNextStart());
					break;
				}
			}
			token.remove();
		}
		
		if (!oneOutgoingFlowGuardSucceeded) {
			throw new IllegalStateException("Model is ill formed, one guard must succeed for a decision node.");
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
	

}
