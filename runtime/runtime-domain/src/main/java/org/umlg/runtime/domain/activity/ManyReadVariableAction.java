package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

import java.util.Collection;

public abstract class ManyReadVariableAction<V> extends ReadVariableAction<V> {

	private static final long serialVersionUID = 704238137687943272L;

	public ManyReadVariableAction() {
	}

	public ManyReadVariableAction(boolean persist, String name) {
		super(persist, name);
	}

	public ManyReadVariableAction(Vertex vertex) {
		super(vertex);
	}
	
	public abstract ManyOutputPin<V> getResult();
	
	protected abstract Collection<V> getVariable();
	
	@Override
	protected boolean execute() {
		getResult().addIncomingToken(new CollectionObjectToken<V>(getResult().getName(), getVariable()));
		return true;
	}

}
