package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;
import org.umlg.runtime.domain.activity.interf.IAcceptCallAction;

public abstract class AcceptCallAction extends AcceptEventAction implements IAcceptCallAction {

	private static final long serialVersionUID = 3098600185311653683L;

	public AcceptCallAction() {
		super();
	}

	public AcceptCallAction(boolean persist, String name) {
		super(persist, name);
	}

	public AcceptCallAction(Vertex vertex) {
		super(vertex);
	}

	//TODO this does not compile in maven on the command line for some reason
//	protected abstract <R, OUT extends ObjectToken<R>> ReturnInformationOutputPin<R, OUT> getReturnInformationOutputPin();

	public abstract ReplyAction getReplyAction();

	@Override
	protected void transferObjectTokensToAction() {
		super.transferObjectTokensToAction();
//		getReturnInformationOutputPin().addOutgoingToken(new SingleObjectToken<Object>(getReturnInformationOutputPin().getName(), "not used except for flow control"));
	}

}
