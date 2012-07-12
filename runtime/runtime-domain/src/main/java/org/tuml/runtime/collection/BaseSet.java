package org.tuml.runtime.collection;

import java.util.HashSet;
import java.util.Set;

import org.tuml.runtime.collection.ocl.BooleanVisitor;
import org.tuml.runtime.collection.ocl.OclStdLibSetOperations;
import org.tuml.runtime.domain.TinkerNode;

public abstract class BaseSet<E> extends BaseCollection<E> implements TinkerSet<E>, OclStdLibSetOperations<E> {

	public BaseSet(TumlRuntimeProperty runtimeProperty) {
		super(runtimeProperty);
		this.internalCollection = new HashSet<E>();
	}
	
	public BaseSet(TinkerNode owner, TumlRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
		this.internalCollection = new HashSet<E>();
	}

	protected Set<E> getInternalSet() {
		return (Set<E>) this.internalCollection;
	}
	
	@Override
	public void clear() {
		maybeLoad();
		for (E e : new HashSet<E>(this.getInternalSet())) {
			this.remove(e);
		}
	}	
	
	@Override
	public Set<E> select(BooleanVisitor<E> v) {
		Set<E> result = new HashSet<E>();
		for (E e : this.internalCollection) {
			if (v.evaluate(e)) {
				result.add(e);
			}
		}
		return result;
	}

}
