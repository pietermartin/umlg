package org.umlg.runtime.collection.persistent;

import org.umlg.runtime.collection.TinkerBag;
import org.umlg.runtime.collection.TumlRuntimeProperty;
import org.umlg.runtime.domain.UmlgNode;

public class TinkerBagImpl<E> extends BaseBag<E> implements TinkerBag<E> {

	public TinkerBagImpl(UmlgNode owner, TumlRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
	}

}
