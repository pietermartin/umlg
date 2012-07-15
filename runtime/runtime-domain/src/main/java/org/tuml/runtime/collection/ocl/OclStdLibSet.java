package org.tuml.runtime.collection.ocl;

import org.tuml.runtime.collection.TinkerBag;
import org.tuml.runtime.collection.TinkerSet;

public interface OclStdLibSet<E> extends OclStdLibCollection<E> {

	@Override
	TinkerSet<E> select(BooleanExpressionEvaluator<E> e);
	
	@Override
	<R> TinkerBag<R> collectNested(BodyExpressionEvaluator<R, E> e);
	
	@Override
	<T, R> TinkerBag<T> collect(BodyExpressionEvaluator<R, E> e);
	
	@Override
	<R> TinkerSet<R> flatten();
	
}
