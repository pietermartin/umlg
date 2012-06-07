package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

public abstract class ForkNodeObjectTokenUnknown<IN extends ObjectToken<?>> extends ForkNode<IN> {

	public ForkNodeObjectTokenUnknown() {
		super();
	}

	public ForkNodeObjectTokenUnknown(boolean persist, String name) {
		super(persist, name);
	}

	public ForkNodeObjectTokenUnknown(Vertex vertex) {
		super(vertex);
	}

}
