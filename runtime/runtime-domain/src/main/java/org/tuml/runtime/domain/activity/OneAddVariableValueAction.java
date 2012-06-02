package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.pgm.Vertex;

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
