package org.umlg.runtime.adaptor;

/**
 * Date: 2013/02/08
 * Time: 8:36 PM
 */
public class UmlgTitanExceptionUtilIml implements UmlgExceptionUtil {

    private static UmlgExceptionUtil INSTANCE = new UmlgTitanExceptionUtilIml();

    private UmlgTitanExceptionUtilIml() {
        super();
    }

    public static UmlgExceptionUtil getInstance() {
        return INSTANCE;
    }

    @Override
    public RuntimeException handle(Exception e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        } else {
            return new RuntimeException(e);
        }
    }

    @Override
    public boolean isNodeNotFoundException(Exception e) {
        throw new RuntimeException("Not yet implemented!");
    }

}
