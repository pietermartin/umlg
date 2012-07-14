package org.tuml.runtime.collection.impl;

import java.util.HashSet;
import java.util.Set;

import org.tuml.runtime.collection.TinkerBag;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.collection.ocl.BodyExpressionEvaluator;
import org.tuml.runtime.collection.ocl.BooleanExpressionWithV;
import org.tuml.runtime.collection.ocl.OclStdLibSet;
import org.tuml.runtime.collection.ocl.OclStdLibSetImpl;
import org.tuml.runtime.domain.TinkerNode;

public abstract class BaseSet<E> extends BaseCollection<E> implements TinkerSet<E>, OclStdLibSet<E> {

	protected OclStdLibSet<E> oclStdLibSet;
	
	public BaseSet(TumlRuntimeProperty runtimeProperty) {
		super(runtimeProperty);
		this.internalCollection = new HashSet<E>();
		this.oclStdLibSet = new OclStdLibSetImpl<E>((Set<E>)this.internalCollection);
		this.oclStdLibCollection = this.oclStdLibSet;
	}
	
	public BaseSet(TinkerNode owner, TumlRuntimeProperty runtimeProperty) {
		super(owner, runtimeProperty);
		this.internalCollection = new HashSet<E>();
		this.oclStdLibSet = new OclStdLibSetImpl<E>((Set<E>)this.internalCollection);
		this.oclStdLibCollection = this.oclStdLibSet;
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
	public <R> TinkerBag<R> collectNested(BodyExpressionEvaluator<R, E> v) {
		maybeLoad();
		return this.oclStdLibSet.collectNested(v);
	}

	@Override
	public <T, R> TinkerBag<T> collect(BodyExpressionEvaluator<R, E> v) {
		maybeLoad();
		return this.oclStdLibSet.collect(v);
	}
	
	@Override
	public <R> TinkerSet<R> flatten() {
		maybeLoad();
		return this.oclStdLibSet.flatten();
	}

	@Override
	public TinkerSet<E> select(BooleanExpressionWithV<E> v) {
		maybeLoad();
		return this.oclStdLibSet.select(v);
	}

}
