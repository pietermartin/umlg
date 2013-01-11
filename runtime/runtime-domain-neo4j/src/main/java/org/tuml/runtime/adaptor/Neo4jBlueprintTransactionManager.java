package org.tuml.runtime.adaptor;

import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;
import org.neo4j.kernel.AbstractGraphDatabase;

import javax.transaction.InvalidTransactionException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 2013/01/09
 * Time: 9:36 PM
 */
public class Neo4jBlueprintTransactionManager implements BlueprintTransactionManager {

    private Map<Integer, Transaction> transactionMap = new HashMap<Integer, Transaction>();
    private Neo4jGraph neo4jGraph;

    Neo4jBlueprintTransactionManager(Neo4jGraph neo4jGraph) {
        this.neo4jGraph = neo4jGraph;
    }

    private Integer getNextTransactionNumber() {
        return this.transactionMap.size();
    }

    public Integer suspend() {
        try {
            TransactionManager txManager = ((AbstractGraphDatabase) this.neo4jGraph.getRawGraph()).getTxManager();
            Transaction transaction = txManager.getTransaction();
            Integer nextTransactionNumber = getNextTransactionNumber();
            this.transactionMap.put(nextTransactionNumber, transaction);
            txManager.suspend();
            return nextTransactionNumber;
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }
    }

    public TransactionalGraph resume(Integer transactionNumber) {
        try {
            Transaction transaction = this.transactionMap.remove(transactionNumber);
            TransactionManager txManager = ((AbstractGraphDatabase) this.neo4jGraph.getRawGraph()).getTxManager();
            txManager.resume(transaction);
            return this.neo4jGraph;
        } catch (InvalidTransactionException e) {
            throw new RuntimeException(e);
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }
    }


}
