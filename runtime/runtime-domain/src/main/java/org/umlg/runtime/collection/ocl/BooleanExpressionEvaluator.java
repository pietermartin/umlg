package org.umlg.runtime.collection.ocl;

@FunctionalInterface
public interface BooleanExpressionEvaluator<E> extends BodyExpressionEvaluator<Boolean, E> {
	@Override
	Boolean evaluate(E e);
}
