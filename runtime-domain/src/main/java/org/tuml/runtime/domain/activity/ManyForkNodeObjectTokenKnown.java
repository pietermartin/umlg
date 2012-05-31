package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class ManyForkNodeObjectTokenKnown<O> extends ForkNodeObjectTokenKnown<O, CollectionObjectToken<O>> {

	public ManyForkNodeObjectTokenKnown() {
		super();
	}

	public ManyForkNodeObjectTokenKnown(boolean persist, String name) {
		super(persist, name);
	}

	public ManyForkNodeObjectTokenKnown(Vertex vertex) {
		super(vertex);
	}

}
