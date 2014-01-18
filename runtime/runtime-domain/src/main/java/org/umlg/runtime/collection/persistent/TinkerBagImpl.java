package org.umlg.runtime.collection.persistent;

import org.umlg.runtime.collection.TinkerBag;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.domain.UmlgNode;

public class TinkerBagImpl<E> extends BaseBag<E> implements TinkerBag<E> {

	public TinkerBagImpl(UmlgNode owner, UmlgRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
	}

}
