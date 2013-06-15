package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;
import org.umlg.runtime.domain.activity.interf.IOneInputPin;

import java.util.Collections;
import java.util.List;

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
