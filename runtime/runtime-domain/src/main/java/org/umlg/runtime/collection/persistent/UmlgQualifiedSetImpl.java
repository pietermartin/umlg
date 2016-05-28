package org.umlg.runtime.collection.persistent;

import org.umlg.runtime.collection.UmlgQualifiedSet;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.domain.UmlgNode;

public class UmlgQualifiedSetImpl<E> extends BaseSet<E> implements UmlgQualifiedSet<E> {

	public UmlgQualifiedSetImpl(UmlgNode owner, PropertyTree propertyTree) {
		super(owner, propertyTree);
	}

	public UmlgQualifiedSetImpl(UmlgNode owner, UmlgRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
	}
	
}
