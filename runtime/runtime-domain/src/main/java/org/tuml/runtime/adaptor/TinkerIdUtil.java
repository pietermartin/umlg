package org.tuml.runtime.adaptor;

import com.tinkerpop.blueprints.pgm.Vertex;

public interface TinkerIdUtil {
	Long getId(Vertex v);
	void setId(Vertex v, Long id);
	int getVersion(Vertex v);
}
