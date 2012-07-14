package org.tuml.runtime.collection.ocl;

import org.tuml.runtime.collection.TinkerSequence;


public interface OclStdLibSequence<E> extends OclStdLibCollection<E> {

	@Override
	TinkerSequence<E> select(BooleanExpressionWithV<E> e);
	
	@Override
	<R> TinkerSequence<R> collectNested(BodyExpressionEvaluator<R, E> e);
	
	@Override
	<T, R> TinkerSequence<T> collect(BodyExpressionEvaluator<R, E> e);
	
	@Override
	<R> TinkerSequence<R> flatten();

}
