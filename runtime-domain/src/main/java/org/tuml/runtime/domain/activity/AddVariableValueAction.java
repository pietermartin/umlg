package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class AddVariableValueAction<V> extends WriteVariableAction<V> {

	private static final long serialVersionUID = -7555694857426822299L;

	public AddVariableValueAction() {
	}

	public AddVariableValueAction(boolean persist, String name) {
		super(persist, name);
	}

	public AddVariableValueAction(Vertex vertex) {
		super(vertex);
	}

}
