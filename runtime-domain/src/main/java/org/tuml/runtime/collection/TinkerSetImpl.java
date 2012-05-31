package org.tuml.runtime.collection;

import java.util.HashSet;
import java.util.Set;

import org.tuml.runtime.domain.CompositionNode;

public class TinkerSetImpl<E> extends BaseSet<E> implements TinkerSet<E> {

	public TinkerSetImpl(CompositionNode owner, String label, boolean isInverse, boolean isManyToMany, boolean composite) {
		super();
		this.internalCollection = new HashSet<E>();
		this.owner = owner;
		this.vertex = owner.getVertex();
		this.label = label;
		this.parentClass = owner.getClass();
		this.inverse = isInverse;
		this.manyToMany = isManyToMany;
		this.composite = composite;
	}
	
	public Set<E> getInternalSet() {
		return (Set<E>) this.internalCollection;
	}

}
