package org.tuml.runtime.collection;

import org.tuml.runtime.domain.CompositionNode;

import com.google.common.collect.HashMultiset;

public class TinkerBagImpl<E> extends BaseBag<E> implements TinkerBag<E> {

	public TinkerBagImpl(CompositionNode owner, String label, boolean isInverse, boolean isManyToMany, boolean composite) {
		super();
		this.internalCollection = HashMultiset.create();
		this.owner = owner;
		this.vertex = owner.getVertex();
		this.label = label;
		this.parentClass = owner.getClass();
		this.inverse = isInverse;
		this.manyToMany = isManyToMany;
		this.composite = composite;
	}

}
