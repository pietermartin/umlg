package org.tuml.runtime.collection.persistent;

import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.TumlNode;

import java.util.Set;

public class TinkerSetImpl<E> extends BaseSet<E> implements TinkerSet<E> {

	public TinkerSetImpl(TumlNode owner, TumlRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
	}
	
	public Set<E> getInternalSet() {
		return (Set<E>) this.internalCollection;
	}

}
