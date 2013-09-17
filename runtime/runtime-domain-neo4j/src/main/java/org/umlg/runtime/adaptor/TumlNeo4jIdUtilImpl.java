package org.umlg.runtime.adaptor;

import com.tinkerpop.blueprints.Vertex;

public class TumlNeo4jIdUtilImpl implements TinkerIdUtil {

	private static TinkerIdUtil INSTANCE = new TumlNeo4jIdUtilImpl();
	
	private TumlNeo4jIdUtilImpl() {
		super();
	}

	public static TinkerIdUtil getInstance() {
		return INSTANCE;
	}
	
	@Override
	public Long getId(Vertex v) {
		return (Long)v.getId();
	}

}
