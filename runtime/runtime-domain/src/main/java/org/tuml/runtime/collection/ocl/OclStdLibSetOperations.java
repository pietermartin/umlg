package org.tuml.runtime.collection.ocl;

import java.util.Set;

/**
 * From ocl spec v2.3.1
 */
public interface OclStdLibSetOperations<E> {

	/**
	 * The subset of set for which expr is true.
     * source->select(iterator | body) =
     * 		source->iterate(iterator; result : Set(T) = Set{} |
     * 			if body then result->including(iterator)
     * 				else result
     * 			endif)
     * select may have at most one iterator variable.
	 */
	Set<E> select(BooleanVisitor<E> e);
}
