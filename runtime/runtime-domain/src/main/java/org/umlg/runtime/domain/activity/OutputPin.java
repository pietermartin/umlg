package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;
import org.umlg.runtime.domain.activity.interf.IOutputPin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class OutputPin<O, OUT extends ObjectToken<O>> extends Pin<O, OUT, OUT> implements IOutputPin<O, OUT> {

	public OutputPin() {
		super();
	}

	public OutputPin(boolean persist, String name) {
		super(persist, name);
	}

	public OutputPin(Vertex vertex) {
		super(vertex);
	}

	public abstract Action getAction();

	@Override
	public Boolean processNextStart() throws NoSuchElementException {
		if (mayContinue()) {
			return executeNode();
		} else {
			return false;
		}
	}

	@Override
	protected Boolean executeNode() {
		List<Boolean> flowResult = new ArrayList<Boolean>();

		setNodeStatus(NodeStatus.ENABLED);
		setNodeStatus(NodeStatus.ACTIVE);

		// execute();

		this.nodeStat.increment();

		for (OUT objectToken : getOutTokens()) {
			// For each out flow add a token
			for (ActivityEdge<OUT> flow : getOutgoing()) {
				@SuppressWarnings("unchecked")
				OUT duplicate = (OUT) objectToken.duplicate(flow.getName());
				addOutgoingToken(duplicate);
			}
			objectToken.remove();
		}
		// Continue each out flow with its tokens
		for (ActivityEdge<OUT> flow : getOutgoing()) {
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
	public List<? extends ObjectFlowKnown<O, OUT>> getIncoming() {
		return Collections.emptyList();
	}

	@Override
	public abstract void copyTokensToStart();
}
