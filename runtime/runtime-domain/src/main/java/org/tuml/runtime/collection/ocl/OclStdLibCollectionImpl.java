package org.tuml.runtime.collection.ocl;

import java.util.Collection;

import org.tuml.runtime.collection.TinkerBag;
import org.tuml.runtime.collection.TinkerOrderedSet;
import org.tuml.runtime.collection.TinkerSequence;
import org.tuml.runtime.collection.TinkerSet;

public abstract class OclStdLibCollectionImpl<E> implements OclStdLibCollection<E> {

	protected Collection<E> collection;
	
	public OclStdLibCollectionImpl(Collection<E> oclStdLibCollection) {
		this.collection = oclStdLibCollection;
	}
	
	@Override
	public boolean equals(OclStdLibCollection<E> c) {
		return this.collection.equals(c);
	}

	@Override
	public int size() {
		return this.collection.size();
	}

	@Override
	public E any(BooleanExpressionEvaluator<E> v) {
		for (E e : this.collection) {
			if (v.evaluate(e)) {
				return e;
			}
		}
		return null;
	}
	
	@Override
	public TinkerSet<E> asSet() {
		return new OclStdLibSetImpl<E>(this.collection);
	}
	
	@Override
	public TinkerOrderedSet<E> asOrderedSet() {
		return new OclStdLibOrderedSetImpl<E>(this.collection);
	}
	
	@Override
	public TinkerSequence<E> asSequence() {
		return new OclStdLibSequenceImpl<E>(this.collection);
	}
	
	@Override
	public TinkerBag<E> asBag() {
		return new OclStdLibBagImpl<E>(this.collection);
	}
	
	@Override
	public <R> R iterate(IterateExpressionAccumulator<R, E> v) {
		R acc = v.initAccumulator();
		for (E e : this.collection) {
			acc = v.accumulate(acc, e);
		}
		return acc;
	}

}
