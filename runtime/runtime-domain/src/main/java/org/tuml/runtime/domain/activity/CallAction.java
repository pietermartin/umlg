package org.tuml.runtime.domain.activity;

import java.util.List;
import java.util.logging.Level;

import org.tuml.runtime.domain.activity.interf.ICallAction;
import org.tuml.runtime.domain.activity.interf.IOutputPin;

import com.tinkerpop.blueprints.Vertex;

public abstract class CallAction extends InvocationAction implements ICallAction {

	private static final long serialVersionUID = 5844558478374903923L;

	public CallAction() {
		super();
	}

	public CallAction(boolean persist, String name) {
		super(persist, name);
	}

	public CallAction(Vertex vertex) {
		super(vertex);
	}

	@Override
	public abstract boolean isSynchronous();

	@Override
	public List<? extends IOutputPin<?, ?>> getResult() {
		return getOutput();
	}
	
	@Override
	protected Boolean executeNode() {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("start executeNode");
			logger.finest(toString());
		}
		preExecute();
		
		//Execute will call either an behavior directly or via an operation. 
		//Either way it will return false if it did not complete
		//When it does complete, now or later it will call postExecute in its ActivityFinalNode and return its result
		return execute();
	}
	
	@Override
	protected Boolean postExecute() {
		//Copy the out parameters of the called behavior to the output pins before continuing
		copyOutParametersToOutputPins();
		return super.postExecute();
	}

	protected abstract void copyOutParametersToOutputPins();

}
