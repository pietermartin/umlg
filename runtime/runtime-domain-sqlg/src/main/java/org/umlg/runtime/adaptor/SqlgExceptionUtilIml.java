package org.umlg.runtime.adaptor;

/**
 * Date: 2013/02/08
 * Time: 8:36 PM
 */
public class SqlgExceptionUtilIml implements UmlgExceptionUtil {

    private static SqlgExceptionUtilIml INSTANCE = new SqlgExceptionUtilIml();

    private SqlgExceptionUtilIml() {
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
        return false;
    }

}
