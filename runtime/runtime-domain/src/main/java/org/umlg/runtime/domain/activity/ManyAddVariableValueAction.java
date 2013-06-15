package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

import java.util.Collection;

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
