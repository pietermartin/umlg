package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

public abstract class AddStructuralFeatureValueAction<V, O> extends WriteStructuralFeatureAction<V, O> {

	private static final long serialVersionUID = 5047776511654951152L;

	public AddStructuralFeatureValueAction() {
		super();
	}

	public AddStructuralFeatureValueAction(boolean persist, String name) {
		super(persist, name);
	}

	public AddStructuralFeatureValueAction(Vertex vertex) {
		super(vertex);
	}

	protected abstract void writeStructuralFeature(O o, V v);

	@Override
	protected boolean execute() {
		writeStructuralFeature(getObject(), getValue());
		getResult().addIncomingToken(new SingleObjectToken<O>(getResult().getName(), getObject()));
		return true;
	}

}
