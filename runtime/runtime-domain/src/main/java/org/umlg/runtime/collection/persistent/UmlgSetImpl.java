package org.umlg.runtime.collection.persistent;

import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.collection.UmlgSet;
import org.umlg.runtime.domain.UmlgEnum;
import org.umlg.runtime.domain.UmlgNode;

import java.util.Set;

public class UmlgSetImpl<E> extends BaseSet<E> implements UmlgSet<E> {

	public UmlgSetImpl(UmlgNode owner, PropertyTree propertyTree) {
		super(owner, propertyTree);
	}

	public UmlgSetImpl(UmlgNode owner, UmlgRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
	}

	public UmlgSetImpl(UmlgEnum owner, UmlgRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
	}

	public Set<E> getInternalSet() {
		return (Set<E>) this.internalCollection;
	}

}
