package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

public abstract class OneReadVariableAction<V> extends ReadVariableAction<V> {

	private static final long serialVersionUID = 5401761478489898373L;

	public OneReadVariableAction() {
	}

	public OneReadVariableAction(boolean persist, String name) {
		super(persist, name);
	}

	public OneReadVariableAction(Vertex vertex) {
		super(vertex);
	}
	
	public abstract OneOutputPin<V> getResult();
	
	protected abstract V getVariable();
	
	@Override
	protected boolean execute() {
		getResult().addIncomingToken(new SingleObjectToken<V>(getResult().getName(), getVariable()));
		return true;
	}

}
