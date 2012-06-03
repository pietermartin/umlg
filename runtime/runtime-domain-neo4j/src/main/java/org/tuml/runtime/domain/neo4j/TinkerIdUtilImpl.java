package org.tuml.runtime.domain.neo4j;

import org.tuml.runtime.adaptor.TinkerIdUtil;

import com.tinkerpop.blueprints.pgm.Vertex;

public class TinkerIdUtilImpl implements TinkerIdUtil {

	private static TinkerIdUtil INSTANCE = new TinkerIdUtilImpl();
	
	private TinkerIdUtilImpl() {
		super();
	}

	public static TinkerIdUtil getInstance() {
		return INSTANCE;
	}
	
	@Override
	public Long getId(Vertex v) {
		return (Long)v.getId();
	}
	
	@Override
	public void setId(Vertex v, Long id) {
		throw new IllegalStateException("Id can not be set using Neo4j");
	}
	
	@Override
	public int getVersion(Vertex v) {
		return -1;
	}	
	
}
