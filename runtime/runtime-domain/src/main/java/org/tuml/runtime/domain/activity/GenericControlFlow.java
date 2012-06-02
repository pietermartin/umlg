package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.pgm.Edge;

public abstract class GenericControlFlow extends ActivityEdge<Token> {

	public GenericControlFlow(Edge edge) {
		super(edge);
	}

}
