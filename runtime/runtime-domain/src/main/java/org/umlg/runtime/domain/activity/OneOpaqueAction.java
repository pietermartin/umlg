package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

public abstract class OneOpaqueAction<R> extends OpaqueAction<R, SingleObjectToken<R>> {

	private static final long serialVersionUID = 1696385621921568073L;

	public OneOpaqueAction() {
		super();
	}

	public OneOpaqueAction(boolean persist, String name) {
		super(persist, name);
	}

	public OneOpaqueAction(Vertex vertex) {
		super(vertex);
	}

	@Override
	protected boolean execute() {
		// Place the result of the body expression on the output pin
		OneOutputPin<R> resultPin = getResultPin();
		if (resultPin != null) {
			resultPin.addIncomingToken(new SingleObjectToken<R>(resultPin.getName(), getBodyExpression()));
		}
		return true;
	}

	protected abstract R getBodyExpression();

	@Override
	protected abstract OneOutputPin<R> getResultPin();
	
}
