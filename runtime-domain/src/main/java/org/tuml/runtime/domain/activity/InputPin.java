package org.tuml.runtime.domain.activity;

import java.util.Collections;
import java.util.List;

import org.tuml.runtime.domain.activity.interf.IInputPin;

import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class InputPin<O, IN extends ObjectToken<O>> extends Pin<O, IN, IN> implements IInputPin<O, IN> {

	private static final long serialVersionUID = 8784471211488847951L;

	public InputPin() {
		super();
	}

	public InputPin(boolean persist, String name) {
		super(persist, name);
	}

	public InputPin(Vertex vertex) {
		super(vertex);
	}

	@Override
	public abstract Action getAction();

	protected Boolean executeNode() {
		Action action = this.getAction();
		if (action.mayContinue()) {
			return action.executeNode();
		} else {
			return false;
		}
	}

	@Override
	public List<? extends ObjectFlowKnown<O, IN>> getOutgoing() {
		return Collections.emptyList();
	}
	
}
