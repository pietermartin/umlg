package org.tuml.runtime.adaptor;

import org.tuml.runtime.test.TumlTestUtil;

/**
 * Date: 2013/01/09
 * Time: 7:35 AM
 */
public class TumlOrientDbTestUtil implements TumlTestUtil {

    private static final TumlOrientDbTestUtil INSTANCE = new  TumlOrientDbTestUtil();

    private TumlOrientDbTestUtil() {

    }

    public static TumlOrientDbTestUtil getInstance() {
        return INSTANCE;
    }

    //TODO
    @Override
    public boolean isTransactionFailedException(Exception e) {
        return e instanceof Exception;
    }
}
