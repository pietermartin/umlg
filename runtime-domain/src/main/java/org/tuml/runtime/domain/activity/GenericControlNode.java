package org.tuml.runtime.domain.activity;

import java.util.List;

import org.tuml.runtime.domain.activity.interf.IActivityEdge;

import com.tinkerpop.blueprints.pgm.Vertex;

public abstract class GenericControlNode extends ControlNode<Token, Token> {

	public GenericControlNode() {
		super();
	}

	public GenericControlNode(boolean persist, String name) {
		super(persist, name);
	}

	public GenericControlNode(Vertex vertex) {
		super(vertex);
	}
	
	@Override
	public abstract List<? extends IActivityEdge<? extends Token>> getIncoming();

}
