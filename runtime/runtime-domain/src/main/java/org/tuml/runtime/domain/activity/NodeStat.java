package org.tuml.runtime.domain.activity;

import com.tinkerpop.blueprints.pgm.Vertex;

public class NodeStat {

	private Vertex vertex;

	public NodeStat(Vertex vertex) {
		super();
		this.vertex = vertex;
	}

	public NodeStat(Vertex vertex, boolean first) {
		super();
		this.vertex = vertex;
		this.vertex.setProperty("executeCount", 0L);
	}

	public long getExecuteCount() {
		return (Long) this.vertex.getProperty("executeCount");
	}

	public void increment() {
		this.vertex.setProperty("executeCount", getExecuteCount() + 1);
	}
	
	@Override
	public String toString() {
		return String.format("NodeStat execute count = %d", getExecuteCount());
	}

}
