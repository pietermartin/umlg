package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.Vertex;
import org.tuml.runtime.domain.activity.interf.IActivityEdge;

import java.util.List;

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
