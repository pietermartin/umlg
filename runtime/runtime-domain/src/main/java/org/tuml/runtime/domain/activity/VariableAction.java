package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

/*
 * V is the type of the variable, if it has multiplicity of many then the elements are of type V
 */
public abstract class VariableAction<V> extends Action {

	private static final long serialVersionUID = 1914040936903897750L;

	public VariableAction() {
		super();
	}

	public VariableAction(boolean persist, String name) {
		super(persist, name);
	}

	public VariableAction(Vertex vertex) {
		super(vertex);
	}

}
