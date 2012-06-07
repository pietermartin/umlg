package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

public abstract class ReadVariableAction<V> extends VariableAction<V> {

	private static final long serialVersionUID = 6936368931697275709L;

	public ReadVariableAction() {
	}

	public ReadVariableAction(boolean persist, String name) {
		super(persist, name);
	}

	public ReadVariableAction(Vertex vertex) {
		super(vertex);
	}
	
}
