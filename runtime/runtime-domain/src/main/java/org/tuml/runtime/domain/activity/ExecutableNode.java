package org.tuml.runtime.domain.activity;

import org.tuml.runtime.domain.activity.interf.IExecutableNode;

import com.tinkerpop.blueprints.Vertex;

public abstract class ExecutableNode extends ActivityNode<ControlToken, ControlToken> implements IExecutableNode {

	private static final long serialVersionUID = 4587351057188861144L;

	public ExecutableNode() {
		super();
	}

	public ExecutableNode(boolean persist, String name) {
		super(persist, name);
	}

	public ExecutableNode(Vertex vertex) {
		super(vertex);
	}

}
