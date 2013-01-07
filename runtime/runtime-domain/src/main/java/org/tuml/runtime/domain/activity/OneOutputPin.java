package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

import java.util.Collections;
import java.util.List;

public abstract class OneOutputPin<O> extends OutputPin<O, SingleObjectToken<O>> {

	public OneOutputPin() {
		super();
	}

	public OneOutputPin(boolean persist, String name) {
		super(persist, name);
	}

	public OneOutputPin(Vertex vertex) {
		super(vertex);
	}

	@Override
	public abstract List<OneObjectFlowKnown<O>> getIncoming();

	@Override
	public List<OneObjectFlowKnown<O>> getOutgoing() {
		return Collections.emptyList();
	}		
	
	@Override
	protected int countNumberOfElementsOnTokens() {
		return getOutTokens().size();
	}	

}
