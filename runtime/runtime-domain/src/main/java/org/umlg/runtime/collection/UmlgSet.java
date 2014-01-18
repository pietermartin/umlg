package org.umlg.runtime.collection;

import org.umlg.runtime.collection.ocl.BodyExpressionEvaluator;
import org.umlg.runtime.collection.ocl.BooleanExpressionEvaluator;
import org.umlg.runtime.collection.ocl.OclStdLibSet;

import java.util.Set;

public interface UmlgSet<E> extends UmlgCollection<E>, Set<E>, OclStdLibSet<E> {
	
	@Override
    UmlgSet<E> select(BooleanExpressionEvaluator<E> e);
	
	@Override
	<R> UmlgBag<R> collectNested(BodyExpressionEvaluator<R, E> e);
	
	@Override
	<T, R> UmlgBag<T> collect(BodyExpressionEvaluator<R, E> e);
	
	@Override
	<R> UmlgSet<R> flatten();
}
