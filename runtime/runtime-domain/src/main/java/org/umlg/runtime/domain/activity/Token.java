package org.umlg.runtime.domain.activity;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import org.umlg.runtime.adaptor.UMLG;

public abstract class Token {

	public static final String TOKEN = "token_";
	protected Vertex vertex;

	public Token(String edgeName) {
		this.vertex = UMLG.getDb().addVertex("Token");
		setEdgeName(edgeName);
	}

	public Token(Vertex vertex) {
		this.vertex = vertex;
	}

	public Vertex getVertex() {
		return vertex;
	}

	public String getEdgeName() {
		return (String) this.vertex.getProperty("edgeName");
	}

	public void setEdgeName(String edgeName) {
		this.vertex.setProperty("edgeName", edgeName);
	}
	
	protected abstract void addEdgeToActivityNode(ActivityNode<? extends Token, ? extends Token> node);
	public abstract <T extends Token> T duplicate(String flowName);
	public abstract void remove();

	protected void removeEdgeFromActivityNode() {
		if (this.vertex.getEdges(Direction.IN, TOKEN + getEdgeName()).iterator().hasNext()) {
			UMLG.getDb().removeEdge(this.vertex.getEdges(Direction.IN).iterator().next());
			if (this.vertex.getEdges(Direction.IN).iterator().hasNext()) {
				throw new IllegalStateException("Token can not have more than one edge!");
			}
		}
	}

}
