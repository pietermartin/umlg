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
    public RuntimeException handle(Exception e) {
        if (e instanceof TransactionFailureException) {
            Throwable transactionFailureExceptionCause = e.getCause();
            if (transactionFailureExceptionCause != null && transactionFailureExceptionCause instanceof RollbackException) {
                Throwable rollbackExceptionCause = transactionFailureExceptionCause.getCause();
                if (rollbackExceptionCause.getCause() != null && (rollbackExceptionCause.getCause() instanceof RuntimeException)) {
                    return (RuntimeException) rollbackExceptionCause.getCause();
                } else if (rollbackExceptionCause instanceof RuntimeException) {
                    return (RuntimeException) rollbackExceptionCause;
                } else {
                    return new RuntimeException(rollbackExceptionCause);
                }
            } else if (transactionFailureExceptionCause instanceof RuntimeException) {
                return (RuntimeException) transactionFailureExceptionCause;
            } else {
                return new RuntimeException(transactionFailureExceptionCause);
            }
        } else if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        } else {
            return new RuntimeException(e);
        }
    }

    @Override
    public boolean isNodeNotFoundException(Exception e) {
        return e instanceof NotFoundException;
    }

}
