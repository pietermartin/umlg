package org.tuml.runtime.adaptor;

import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.TransactionFailureException;

import javax.transaction.RollbackException;

/**
 * Date: 2013/02/08
 * Time: 8:36 PM
 */
public class TumlNeo4jExceptionUtilIml implements TumlExceptionUtil {

    private static TumlExceptionUtil INSTANCE = new TumlNeo4jExceptionUtilIml();

    private TumlNeo4jExceptionUtilIml() {
        super();
    }

    public static TumlExceptionUtil getInstance() {
        return INSTANCE;
    }

    @Override
    public void handle(Exception e) {
        if (e instanceof TransactionFailureException) {
            Throwable transactionFailureExceptionCause = e.getCause();
            if (transactionFailureExceptionCause != null && transactionFailureExceptionCause instanceof RollbackException) {
                Throwable rollbackExceptionCause = transactionFailureExceptionCause.getCause();
                if (rollbackExceptionCause.getCause() != null && (rollbackExceptionCause.getCause() instanceof RuntimeException)) {
                    throw (RuntimeException) rollbackExceptionCause.getCause();
                } else if (rollbackExceptionCause instanceof RuntimeException) {
                    throw (RuntimeException) rollbackExceptionCause;
                } else {
                    throw new RuntimeException(rollbackExceptionCause);
                }
            } else if (transactionFailureExceptionCause instanceof RuntimeException) {
                throw (RuntimeException) transactionFailureExceptionCause;
            } else {
                throw new RuntimeException(transactionFailureExceptionCause);
            }
        } else if (e instanceof RuntimeException) {
            throw (RuntimeException) e;
        } else {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isNodeNotFoundException(Exception e) {
        return e instanceof NotFoundException;
    }

}
