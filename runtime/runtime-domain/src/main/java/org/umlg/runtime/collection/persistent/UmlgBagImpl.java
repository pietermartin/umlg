package org.umlg.runtime.collection.persistent;

import org.umlg.runtime.collection.UmlgBag;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.domain.UmlgNode;

public class UmlgBagImpl<E> extends BaseBag<E> implements UmlgBag<E> {

	public UmlgBagImpl(UmlgNode owner, PropertyTree propertyTree) {
		super(owner, propertyTree);
	}

	public UmlgBagImpl(UmlgNode owner, UmlgRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
	}

}
