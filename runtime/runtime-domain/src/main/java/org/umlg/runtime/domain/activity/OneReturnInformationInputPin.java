package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

public abstract class OneReturnInformationInputPin<O> extends ReturnInformationInputPin<O,SingleObjectToken<O>> {

	public OneReturnInformationInputPin() {
		super();
	}

	public OneReturnInformationInputPin(boolean persist, String name) {
		super(persist, name);
	}

	public OneReturnInformationInputPin(Vertex vertex) {
		super(vertex);
	}

	@Override
	public abstract ReplyAction getAction();
	
	@Override
	protected int countNumberOfElementsOnTokens() {
		return getInTokens().size();
	}
}
