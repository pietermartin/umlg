package org.tuml.runtime.domain;

import java.util.List;

import org.tuml.runtime.collection.Qualifier;
import org.tuml.runtime.collection.TumlRuntimeProperty;

import com.tinkerpop.blueprints.Vertex;

public interface TinkerNode extends PersistentObject {
	Vertex getVertex();
	boolean isTinkerRoot();
	void initialiseProperties();
	void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty);
	List<Qualifier> getQualifiers(TumlRuntimeProperty tumlRuntimeProperty, TinkerNode node);
	void delete();
	int getSize(TumlRuntimeProperty tumlRuntimeProperty);
}
