package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class ForkNodeObjectTokenKnown<O,IN extends ObjectToken<O>> extends ForkNode<IN> {

	public ForkNodeObjectTokenKnown() {
		super();
	}

	public ForkNodeObjectTokenKnown(boolean persist, String name) {
		super(persist, name);
	}

	public ForkNodeObjectTokenKnown(Vertex vertex) {
		super(vertex);
	}

}
