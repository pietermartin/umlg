package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;

public abstract class OneForkNodeObjectTokenKnown<O> extends ForkNodeObjectTokenKnown<O, SingleObjectToken<O>> {

	public OneForkNodeObjectTokenKnown() {
		super();
	}

	public OneForkNodeObjectTokenKnown(boolean persist, String name) {
		super(persist, name);
	}

	public OneForkNodeObjectTokenKnown(Vertex vertex) {
		super(vertex);
	}

}
