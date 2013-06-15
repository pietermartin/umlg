package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

public abstract class OneAddVariableValueAction<V> extends AddVariableValueAction<V> {

	public OneAddVariableValueAction() {
	}

	public OneAddVariableValueAction(boolean persist, String name) {
		super(persist, name);
	}

	public OneAddVariableValueAction(Vertex vertex) {
		super(vertex);
	}
	
	protected abstract V getVariable();
}
