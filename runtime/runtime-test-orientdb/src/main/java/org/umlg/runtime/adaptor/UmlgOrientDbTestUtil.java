package org.umlg.runtime.adaptor;

import org.umlg.runtime.test.UmlgTestUtil;

/**
 * Date: 2013/01/09
 * Time: 7:35 AM
 */
public class UmlgOrientDbTestUtil implements UmlgTestUtil {

    private static final UmlgOrientDbTestUtil INSTANCE = new UmlgOrientDbTestUtil();

    private UmlgOrientDbTestUtil() {

    }

    public static UmlgOrientDbTestUtil getInstance() {
        return INSTANCE;
    }

    //TODO
    @Override
    public boolean isTransactionFailedException(Exception e) {
        return e instanceof Exception;
    }
}
