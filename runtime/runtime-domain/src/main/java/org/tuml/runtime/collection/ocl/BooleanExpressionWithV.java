package org.tuml.runtime.collection.ocl;

public interface BooleanExpressionWithV<E> extends BodyExpressionEvaluator<Boolean, E> {
	@Override
	Boolean evaluate(E e);
}
