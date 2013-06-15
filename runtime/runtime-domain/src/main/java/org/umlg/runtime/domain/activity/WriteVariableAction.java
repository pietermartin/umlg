package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

public abstract class WriteVariableAction<V> extends VariableAction<V> {

	private static final long serialVersionUID = 906884741983142841L;

	public WriteVariableAction() {
		super();
	}

	public WriteVariableAction(boolean persist, String name) {
		super(persist, name);
	}

	public WriteVariableAction(Vertex vertex) {
		super(vertex);
	}
	
	public abstract V getValue();
	protected abstract void writeVariable(V v);
	
	@Override
	protected boolean execute() {
		writeVariable(getValue());
		return true;
	}	

}
