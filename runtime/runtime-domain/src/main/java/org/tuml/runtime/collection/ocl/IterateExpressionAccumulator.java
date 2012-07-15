package org.tuml.runtime.collection.ocl;


public interface IterateExpressionAccumulator<R, E> {
	R accumulate(R acc, E e);
	R initAccumulator();
}
