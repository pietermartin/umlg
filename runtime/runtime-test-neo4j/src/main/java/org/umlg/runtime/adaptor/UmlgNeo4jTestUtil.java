package org.umlg.runtime.adaptor;

import org.neo4j.graphdb.TransactionFailureException;
import org.umlg.runtime.test.UmlgTestUtil;

/**
 * Date: 2013/01/09
 * Time: 7:45 AM
 */
public class UmlgNeo4jTestUtil implements UmlgTestUtil {

    public static final UmlgNeo4jTestUtil INSTANCE = new UmlgNeo4jTestUtil();

    private UmlgNeo4jTestUtil() {
    }

    public static UmlgNeo4jTestUtil getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isTransactionFailedException(Exception e) {
        return e instanceof TransactionFailureException;
    }

}
