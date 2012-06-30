package org.tuml.runtime.collection;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.domain.TinkerNode;

import com.tinkerpop.blueprints.Edge;

public class TinkerQualifiedSetImpl<E> extends BaseSet<E> implements TinkerQualifiedSet<E> {

	public TinkerQualifiedSetImpl(TinkerNode owner, TumlRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
		this.index = GraphDb.getDb().getIndex(owner.getUid() + ":::" + getLabel(), Edge.class);
		if (this.index == null) {
			this.index = GraphDb.getDb().createIndex(owner.getUid() + ":::" + getLabel(), Edge.class);
		}
	}
	
}
