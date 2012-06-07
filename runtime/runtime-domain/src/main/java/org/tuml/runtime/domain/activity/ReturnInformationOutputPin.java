package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

public abstract class ReturnInformationOutputPin<O,OUT extends ObjectToken<O>> extends OutputPin<O,OUT> {

	private static final long serialVersionUID = 1L;

	public ReturnInformationOutputPin() {
		super();
	}

	public ReturnInformationOutputPin(boolean persist, String name) {
		super(persist, name);
	}

	public ReturnInformationOutputPin(Vertex vertex) {
		super(vertex);
	}

	protected abstract ReturnInformationInputPin<O,OUT> getReturnInformationInputPin();
}
