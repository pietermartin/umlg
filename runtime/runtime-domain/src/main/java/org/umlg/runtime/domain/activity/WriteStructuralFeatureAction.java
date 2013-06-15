package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;


public abstract class WriteStructuralFeatureAction<V, O> extends StructuralFeatureAction<O> {

	private static final long serialVersionUID = 1270124661628360416L;
	public WriteStructuralFeatureAction() {
		super();
	}

	public WriteStructuralFeatureAction(boolean persist, String name) {
		super(persist, name);
	}

	public WriteStructuralFeatureAction(Vertex vertex) {
		super(vertex);
	}
	
	public abstract V getValue();
	public abstract OutputPin<O, SingleObjectToken<O>> getResult();

}
