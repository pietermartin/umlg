package org.tuml.runtime.domain;

import com.tinkerpop.blueprints.pgm.Vertex;

public interface TinkerNode extends PersistentObject {
	Vertex getVertex();
	boolean isTinkerRoot();
	void initialiseProperties();
}
