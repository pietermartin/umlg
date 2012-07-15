package org.tuml.runtime.collection.ocl;

import org.tuml.runtime.collection.TinkerBag;
import org.tuml.runtime.collection.TinkerOrderedSet;


public interface OclStdLibOrderedSet<E> extends OclStdLibCollection<E> {

	@Override
	TinkerOrderedSet<E> select(BooleanExpressionEvaluator<E> e);
	
	@Override
	<R> TinkerBag<R> collectNested(BodyExpressionEvaluator<R, E> e);
	
	@Override
	<T, R> TinkerBag<T> collect(BodyExpressionEvaluator<R, E> e);

	@Override
	<R> TinkerOrderedSet<R> flatten();
	
}
