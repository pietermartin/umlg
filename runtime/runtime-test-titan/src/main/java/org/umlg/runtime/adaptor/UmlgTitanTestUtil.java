package org.umlg.runtime.adaptor;

import org.umlg.runtime.test.TumlTestUtil;

/**
 * Date: 2013/01/09
 * Time: 7:35 AM
 */
public class UmlgTitanTestUtil implements TumlTestUtil {

    private static final UmlgTitanTestUtil INSTANCE = new  UmlgTitanTestUtil();

    private UmlgTitanTestUtil() {

    }

    public static UmlgTitanTestUtil getInstance() {
        return INSTANCE;
    }

    //TODO
    @Override
    public boolean isTransactionFailedException(Exception e) {
        return e instanceof Exception;
    }
}
