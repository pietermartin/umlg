package org.tuml.runtime.adaptor;

import com.tinkerpop.blueprints.TransactionalGraph;

/**
 * Date: 2013/01/09
 * Time: 9:36 PM
 */
public interface BlueprintTransactionManager {
    Integer suspend();
    TransactionalGraph resume(Integer transactionNumber);
}
