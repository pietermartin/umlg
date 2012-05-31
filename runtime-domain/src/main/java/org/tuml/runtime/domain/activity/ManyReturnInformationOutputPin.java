package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class ManyReturnInformationOutputPin<O> extends ReturnInformationOutputPin<O,CollectionObjectToken<O>> {

	public ManyReturnInformationOutputPin() {
		super();
	}

	public ManyReturnInformationOutputPin(boolean persist, String name) {
		super(persist, name);
	}

	public ManyReturnInformationOutputPin(Vertex vertex) {
		super(vertex);
	}

	protected abstract ManyReturnInformationInputPin<O> getReturnInformationInputPin();
}
