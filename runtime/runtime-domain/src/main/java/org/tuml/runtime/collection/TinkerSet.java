package org.tuml.runtime.collection;

import org.tuml.runtime.collection.ocl.BodyExpressionEvaluator;
import org.tuml.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.tuml.runtime.collection.ocl.OclStdLibSet;

import java.util.Set;

public interface TinkerSet<E> extends TinkerCollection<E>, Set<E>, OclStdLibSet<E> {
	
	@Override
	TinkerSet<E> select(BooleanExpressionEvaluator<E> e);
	
	@Override
	<R> TinkerBag<R> collectNested(BodyExpressionEvaluator<R, E> e);
	
	@Override
	<T, R> TinkerBag<T> collect(BodyExpressionEvaluator<R, E> e);
	
	@Override
	<R> TinkerSet<R> flatten();
}
