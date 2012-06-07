package org.tuml.runtime.domain.activity;

import java.util.Collections;
import java.util.List;

import org.tuml.runtime.domain.activity.interf.IOneInputPin;

import com.tinkerpop.blueprints.Vertex;

public abstract class OneInputPin<O> extends InputPin<O, SingleObjectToken<O>> implements IOneInputPin<O> {

	public OneInputPin() {
		super();
	}

	public OneInputPin(boolean persist, String name) {
		super(persist, name);
	}

	public OneInputPin(Vertex vertex) {
		super(vertex);
	}

	@Override
	public List<OneObjectFlowKnown<O>> getIncoming() {
		return Collections.emptyList();
	}

	@Override
	public abstract List<OneObjectFlowKnown<O>> getOutgoing();
	
	@Override
	protected int countNumberOfElementsOnTokens() {
		return getInTokens().size();
	}	
	
}
