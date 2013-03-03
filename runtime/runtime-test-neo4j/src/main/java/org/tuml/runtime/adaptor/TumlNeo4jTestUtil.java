package org.tuml.runtime.adaptor;

import org.neo4j.graphdb.TransactionFailureException;
import org.tuml.runtime.test.TumlTestUtil;

/**
 * Date: 2013/01/09
 * Time: 7:45 AM
 */
public class TumlNeo4jTestUtil implements TumlTestUtil {

    public static final TumlNeo4jTestUtil INSTANCE = new TumlNeo4jTestUtil();

    private TumlNeo4jTestUtil() {
    }

    public static TumlNeo4jTestUtil getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isTransactionFailedException(Exception e) {
        return e instanceof TransactionFailureException;
    }

}
