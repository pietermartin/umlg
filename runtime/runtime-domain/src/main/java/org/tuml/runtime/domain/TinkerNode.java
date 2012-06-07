package org.tuml.runtime.domain;

import org.tuml.runtime.collection.TumlRuntimeProperty;

import com.tinkerpop.blueprints.Vertex;

public interface TinkerNode extends PersistentObject {
	Vertex getVertex();
	boolean isTinkerRoot();
	void initialiseProperties();
	void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty);
	void delete();
}
