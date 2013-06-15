package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

public abstract class OneReturnInformationOutputPin<O> extends ReturnInformationOutputPin<O,SingleObjectToken<O>> {

	public OneReturnInformationOutputPin() {
		super();
	}

	public OneReturnInformationOutputPin(boolean persist, String name) {
		super(persist, name);
	}

	public OneReturnInformationOutputPin(Vertex vertex) {
		super(vertex);
	}

	protected abstract OneReturnInformationInputPin<O> getReturnInformationInputPin();
	
	@Override
	protected int countNumberOfElementsOnTokens() {
		return getOutTokens().size();
	}
}
