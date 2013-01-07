package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;
import org.tuml.runtime.domain.activity.interf.ICallOperationAction;
import org.tuml.runtime.domain.activity.interf.IInputPin;

public abstract class CallOperationAction extends CallAction implements ICallOperationAction {

	private static final long serialVersionUID = 5673405797104866039L;

	public CallOperationAction() {
		super();
	}

	public CallOperationAction(boolean persist, String name) {
		super(persist, name);
	}

	public CallOperationAction(Vertex vertex) {
		super(vertex);
	}
	
	@Override
	public abstract IInputPin<?, ?> getTarget();
	
	//TODO implements this
	@Override
	public boolean isSynchronous() {
		return true;
	}
	
}
