package org.umlg.runtime.collection.memory;

import org.umlg.runtime.collection.UmlgBag;
import org.umlg.runtime.collection.UmlgSet;
import org.umlg.runtime.collection.ocl.BodyExpressionEvaluator;
import org.umlg.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.umlg.runtime.collection.ocl.OclStdLibSet;
import org.umlg.runtime.collection.ocl.OclStdLibSetImpl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UmlgMemorySet<E> extends UmlgMemoryCollection<E> implements UmlgSet<E> {

	private OclStdLibSet<E> oclStdLibSet;

	public UmlgMemorySet() {
		super();
		this.internalCollection = new HashSet<E>();
		this.oclStdLibSet = new OclStdLibSetImpl<E>((Set<E>) this.internalCollection);
		this.oclStdLibCollection = this.oclStdLibSet;
	}
	
	public UmlgMemorySet(Collection<E> c) {
		super();
		this.internalCollection = new HashSet<E>(c);
		this.oclStdLibSet = new OclStdLibSetImpl<E>((Set<E>) this.internalCollection);
		this.oclStdLibCollection = this.oclStdLibSet;
	}

	@Override
	public UmlgSet<E> select(BooleanExpressionEvaluator<E> e) {
		return this.oclStdLibSet.select(e);
	}

	@Override
	public <R> UmlgBag<R> collectNested(BodyExpressionEvaluator<R, E> e) {
		return this.oclStdLibSet.collectNested(e);
	}

	@Override
	public <T, R> UmlgBag<T> collect(BodyExpressionEvaluator<R, E> e) {
		return this.oclStdLibSet.collect(e);
	}

	@Override
	public <T2> UmlgSet<T2> flatten() {
		return oclStdLibSet.flatten();
	}

	@Override
	public UmlgSet<E> union(UmlgSet<? extends  E> s) {
		return oclStdLibSet.union(s);
	}

	@Override
	public UmlgBag<E> union(UmlgBag<? extends E> bag) {
		return oclStdLibSet.union(bag);
	}

	@Override
	public Boolean equals(UmlgSet<E> s) {
		return oclStdLibSet.equals(s);
	}

	@Override
	public UmlgSet<E> intersection(UmlgSet<E> s) {
		return oclStdLibSet.intersection(s);
	}

	@Override
	public UmlgSet<E> intersection(UmlgBag<E> bag) {
		return oclStdLibSet.intersection(bag);
	}

	@Override
	public UmlgSet<E> subtract(UmlgSet<E> s) {
		return oclStdLibSet.subtract(s);
	}

	@Override
	public UmlgSet<E> including(E e) {
		return oclStdLibSet.including(e);
	}

	@Override
	public UmlgSet<E> excluding(E e) {
		return oclStdLibSet.excluding(e);
	}

	@Override
	public UmlgSet<E> symmetricDifference(UmlgSet<E> s) {
		return oclStdLibSet.symmetricDifference(s);
	}

}
