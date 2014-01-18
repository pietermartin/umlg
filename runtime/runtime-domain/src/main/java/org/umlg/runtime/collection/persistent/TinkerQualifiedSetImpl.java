package org.umlg.runtime.collection.persistent;

import org.umlg.runtime.collection.TinkerQualifiedSet;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.domain.UmlgNode;

public class TinkerQualifiedSetImpl<E> extends BaseSet<E> implements TinkerQualifiedSet<E> {

	public TinkerQualifiedSetImpl(UmlgNode owner, UmlgRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
//		this.index = GraphDb.getDb().getIndex(getQualifiedName(), Edge.class);
	}
	
}
