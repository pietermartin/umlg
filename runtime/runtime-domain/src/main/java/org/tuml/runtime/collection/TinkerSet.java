package org.tuml.runtime.collection;

import java.util.Set;

import org.tuml.runtime.collection.ocl.BodyExpressionEvaluator;
import org.tuml.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.tuml.runtime.collection.ocl.OclStdLibSet;

public interface TinkerSet<E> extends TinkerCollection<E>, Set<E>, OclStdLibSet<E> {
	
	@Override
	TinkerSet<E> select(BooleanExpressionEvaluator<E> e);
	
	@Override
	<R> TinkerBag<R> collectNested(BodyExpressionEvaluator<R, E> e);
	
	@Override
	<R> TinkerBag<R> collect(BodyExpressionEvaluator<R, E> e);
	
	@Override
	<R> TinkerSet<R> flatten();
}
