package org.tuml.runtime.collection.ocl;

import java.util.Collection;

import org.tuml.runtime.collection.TinkerBag;
import org.tuml.runtime.collection.TinkerCollection;
import org.tuml.runtime.collection.TinkerOrderedSet;
import org.tuml.runtime.collection.TinkerSequence;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.domain.ocl.OclState;

public class OclStdLibCollectionImpl<E> implements OclStdLibCollection<E> {

	protected Collection<E> collection;
	
	public OclStdLibCollectionImpl(Collection<E> oclStdLibCollection) {
		this.collection = oclStdLibCollection;
	}
	
	@Override
	public boolean equals(TinkerCollection<E> c) {
		throw new RuntimeException("Not implemented");
	}


	@Override
	public boolean notEquals(TinkerCollection<E> c) {
		throw new RuntimeException("Not implemented");
	}


	@Override
	public int size() {
		return this.collection.size();
	}


	@Override
	public boolean includes(E t) {
		throw new RuntimeException("Not implemented");
	}


	@Override
	public boolean excludes(E t) {
		throw new RuntimeException("Not implemented");
	}


	@Override
	public int count(E e) {
		throw new RuntimeException("Not implemented");
	}


	@Override
	public Boolean includesAll(TinkerCollection<E> c) {
		throw new RuntimeException("Not implemented");
	}


	@Override
	public Boolean excludesAll(TinkerCollection<E> c) {
		throw new RuntimeException("Not implemented");
	}


	@Override
	public boolean isEmpty() {
		throw new RuntimeException("Not implemented");
	}


	@Override
	public Boolean notEmpty() {
		throw new RuntimeException("Not implemented");
	}


	@Override
	public E max() {
		throw new RuntimeException("Not implemented");
	}


	@Override
	public E min() {
		throw new RuntimeException("Not implemented");
	}


	@Override
	public E sum() {
		throw new RuntimeException("Not implemented");
	}


	@Override
	public TinkerSet<?> product(TinkerCollection<E> c) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public <T2> TinkerCollection<T2> flatten() {
		// TODO Auto-generated method stub
		return null;
	}

	
	/***************************************************
	 * Iterate goodies
	 ***************************************************/
	
	@Override
	public TinkerCollection<E> select(BooleanExpressionEvaluator<E> e) {
		throw new RuntimeException("Not implemented");
	}


	@Override
	public <R> TinkerCollection<R> collect(BodyExpressionEvaluator<R, E> e) {
		throw new RuntimeException("Not implemented");
	}


	@Override
	public <R> TinkerCollection<R> collectNested(BodyExpressionEvaluator<R, E> e) {
		throw new RuntimeException("Not implemented");
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
		return OclStdLibSetImpl.<E>get(this.collection);
	}
	
	@Override
	public TinkerOrderedSet<E> asOrderedSet() {
		return OclStdLibOrderedSetImpl.<E>get(this.collection);
	}
	
	@Override
	public TinkerSequence<E> asSequence() {
		return OclStdLibSequenceImpl.<E>get(this.collection);
	}
	
	@Override
	public TinkerBag<E> asBag() {
		return OclStdLibBagImpl.<E>get(this.collection);
	}
	
	@Override
	public <R> R iterate(IterateExpressionAccumulator<R, E> v) {
		R acc = v.initAccumulator();
		for (E e : this.collection) {
			acc = v.accumulate(acc, e);
		}
		return acc;
	}

	/*******************************
	 * OclAny  
	 *******************************/
	
	@Override
	public boolean equals(Object object) {
		throw new RuntimeException("Not implemented");
//		return null;
	}

	@Override
	public boolean notEquals(Object object) {
		throw new RuntimeException("Not implemented");
//		return null;
	}

	@Override
	public Boolean oclIsNew() {
		throw new RuntimeException("Not implemented");
//		return null;
	}

	@Override
	public Boolean oclIsUndefined() {
		throw new RuntimeException("Not implemented");
//		return null;
	}

	@Override
	public Boolean oclIsInvalid() {
		throw new RuntimeException("Not implemented");
//		return null;
	}

	@Override
	public <T> T oclAsType(T classifier) {
		throw new RuntimeException("Not implemented");
//		return null;
	}

	@Override
	public Boolean oclIsTypeOf(Object object) {
		throw new RuntimeException("Not implemented");
//		return null;
	}

	@Override
	public Boolean oclIsKindOf(Object object) {
		throw new RuntimeException("Not implemented");
//		return null;
	}

	@Override
	public Boolean oclIsInState(OclState state) {
		throw new RuntimeException("Not implemented");
//		return null;
	}

	@Override
	public <T  extends Object> Class<T> oclType() {
		throw new RuntimeException("Not implemented");
//		return null;
	}

	@Override
	public String oclLocale() {
		throw new RuntimeException("Not implemented");
//		return null;
	}

}
