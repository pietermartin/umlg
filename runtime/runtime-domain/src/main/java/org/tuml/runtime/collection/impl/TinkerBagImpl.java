package org.tuml.runtime.collection.impl;

import org.tuml.runtime.collection.TinkerBag;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.TinkerNode;

public class TinkerBagImpl<E> extends BaseBag<E> implements TinkerBag<E> {

	public TinkerBagImpl(TinkerNode owner, TumlRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
	}

}
