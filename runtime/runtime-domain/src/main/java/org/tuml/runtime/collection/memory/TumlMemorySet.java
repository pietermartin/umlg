package org.tuml.runtime.collection.memory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.tuml.runtime.collection.TinkerBag;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.ocl.BodyExpressionEvaluator;
import org.tuml.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.tuml.runtime.collection.ocl.OclStdLibSet;
import org.tuml.runtime.collection.ocl.OclStdLibSetImpl;

public class TumlMemorySet<E> extends TumlMemoryCollection<E> implements TinkerSet<E> {

	private OclStdLibSet<E> oclStdLibSet;

	public TumlMemorySet() {
		super();
		this.internalCollection = new HashSet<E>();
		this.oclStdLibSet = new OclStdLibSetImpl<E>((Set<E>) this.internalCollection);
		this.oclStdLibCollection = this.oclStdLibSet;
	}
	
	public TumlMemorySet(Collection<E> c) {
		super();
		this.internalCollection = new HashSet<E>(c);
		this.oclStdLibSet = new OclStdLibSetImpl<E>((Set<E>) this.internalCollection);
		this.oclStdLibCollection = this.oclStdLibSet;
	}

	@Override
	public TinkerSet<E> select(BooleanExpressionEvaluator<E> e) {
		return this.oclStdLibSet.select(e);
	}

	@Override
	public <R> TinkerBag<R> collectNested(BodyExpressionEvaluator<R, E> e) {
		return this.oclStdLibSet.collectNested(e);
	}

	@Override
	public <T, R> TinkerBag<T> collect(BodyExpressionEvaluator<R, E> e) {
		return this.oclStdLibSet.collect(e);
	}

	@Override
	public <T2> TinkerSet<T2> flatten() {
		return oclStdLibSet.flatten();
	}

	@Override
	public TinkerSet<E> union(TinkerSet<E> s) {
		return oclStdLibSet.union(s);
	}

	@Override
	public TinkerBag<E> union(TinkerBag<E> bag) {
		return oclStdLibSet.union(bag);
	}

	@Override
	public Boolean equals(TinkerSet<E> s) {
		return oclStdLibSet.equals(s);
	}

	@Override
	public TinkerSet<E> intersection(TinkerSet<E> s) {
		return oclStdLibSet.intersection(s);
	}

	@Override
	public TinkerSet<E> intersection(TinkerBag<E> bag) {
		return oclStdLibSet.intersection(bag);
	}

	@Override
	public TinkerSet<E> subtract(TinkerSet<E> s) {
		return oclStdLibSet.subtract(s);
	}

	@Override
	public TinkerSet<E> including(E e) {
		return oclStdLibSet.including(e);
	}

	@Override
	public TinkerSet<E> excluding(E e) {
		return oclStdLibSet.excluding(e);
	}

	@Override
	public TinkerSet<E> symmetricDifference(TinkerSet<E> s) {
		return oclStdLibSet.symmetricDifference(s);
	}

}
