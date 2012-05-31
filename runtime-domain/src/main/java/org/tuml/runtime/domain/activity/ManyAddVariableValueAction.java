package org.tuml.runtime.domain.activity;

import java.util.Collection;

import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class ManyAddVariableValueAction<V> extends AddVariableValueAction<V> {

	public ManyAddVariableValueAction() {
	}

	public ManyAddVariableValueAction(boolean persist, String name) {
		super(persist, name);
	}

	public ManyAddVariableValueAction(Vertex vertex) {
		super(vertex);
	}
	
	protected abstract Collection<V> getVariable();
	
	//TODO implement insertAt
	protected void writeVariable(V v) {
		getVariable().add(v);
	}

}
