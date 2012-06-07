package org.tuml.runtime.domain.activity;

import java.util.Collection;

import org.tuml.runtime.adaptor.GraphDb;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public abstract class ObjectToken<O> extends Token {

	public ObjectToken(Vertex vertex) {
		super(vertex);
	}

	public ObjectToken(String edgeName) {
		super(edgeName);
	}

	public ObjectToken(String edgeName, O object) {
		super(edgeName);
	}

	@Override
	protected void addEdgeToActivityNode(ActivityNode<? extends Token, ? extends Token> node) {
		Edge edge = GraphDb.getDb().addEdge(null, node.vertex, getVertex(), TOKEN + getEdgeName());
		edge.setProperty("tokenClass", getClass().getName());
		edge.setProperty("outClass", node.getClass().getName());
	}

	public abstract void remove();
	
	public abstract Object getObject();
	
	public abstract int getNumberOfElements();
	
	public abstract Collection<O> getElements();

}
