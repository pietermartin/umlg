package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

import java.util.Collections;
import java.util.List;

public abstract class FinalNode extends GenericControlNode {

	public FinalNode() {
		super();
	}

	public FinalNode(Vertex vertex) {
		super(vertex);
	}

	public FinalNode(boolean persist, String name) {
		super(persist, name);
	}

	@Override
	public List<ActivityEdge<Token>> getOutgoing() {
		return Collections.<ActivityEdge<Token>> emptyList();
	}

	@Override
	public boolean mayContinue() {
		return true;
	}

	@Override
	protected Boolean executeNode() {
		Boolean result = super.executeNode();
		// TinkerActivityFinalNodeBlockingQueue.INSTANCE.complete(getActivity().getUid());
		// If call back then call it.
		CallAction callAction = getActivity().getCallAction();
		if (callAction != null) {
			return callAction.postExecute();
		} else {
			return result;
		}
	}

}
