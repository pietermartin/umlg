package org.tuml.runtime.collection;

import java.util.HashSet;
import java.util.Set;

import org.tuml.runtime.domain.TinkerNode;

public class TinkerSetImpl<E> extends BaseSet<E> implements TinkerSet<E> {

	public TinkerSetImpl(TinkerNode owner, TumlRuntimeProperty multiplicity) {
		super();
		this.internalCollection = new HashSet<E>();
		this.owner = owner;
		this.vertex = owner.getVertex();
		this.parentClass = owner.getClass();
		this.tumlRuntimeProperty = multiplicity;
	}
	
	public Set<E> getInternalSet() {
		return (Set<E>) this.internalCollection;
	}

}
