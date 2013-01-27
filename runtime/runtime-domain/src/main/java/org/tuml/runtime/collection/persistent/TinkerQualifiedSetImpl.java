package org.tuml.runtime.collection.persistent;

import com.tinkerpop.blueprints.Edge;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.collection.TinkerQualifiedSet;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.TumlNode;

public class TinkerQualifiedSetImpl<E> extends BaseSet<E> implements TinkerQualifiedSet<E> {

	public TinkerQualifiedSetImpl(TumlNode owner, TumlRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
		this.index = GraphDb.getDb().getIndex(owner.getUid() + INDEX_SEPARATOR + getQualifiedName(), Edge.class);
		if (this.index == null) {
			this.index = GraphDb.getDb().createIndex(owner.getUid() + INDEX_SEPARATOR + getQualifiedName(), Edge.class);
		}
	}
	
}
