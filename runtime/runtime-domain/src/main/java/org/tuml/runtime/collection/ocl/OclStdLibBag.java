package org.tuml.runtime.collection.ocl;

import org.tuml.runtime.collection.TinkerBag;


public interface OclStdLibBag<E> extends OclStdLibCollection<E> {

	@Override
	TinkerBag<E> select(BooleanExpressionWithV<E> e);
	
	@Override
	<R> TinkerBag<R> collectNested(BodyExpressionEvaluator<R, E> e);

	@Override
	<T, R> TinkerBag<T> collect(BodyExpressionEvaluator<R, E> e);
	
	@Override
	<R> TinkerBag<R> flatten();

}
