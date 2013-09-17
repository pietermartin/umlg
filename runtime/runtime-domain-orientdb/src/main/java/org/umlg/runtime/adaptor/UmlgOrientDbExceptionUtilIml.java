package org.umlg.runtime.adaptor;

/**
 * Date: 2013/09/17
 * Time: 7:35 AM
 */
public class UmlgOrientDbExceptionUtilIml implements UmlgExceptionUtil {

    private static UmlgExceptionUtil INSTANCE = new UmlgOrientDbExceptionUtilIml();

    private UmlgOrientDbExceptionUtilIml() {
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
//        return e instanceof NotFoundException;
        throw new RuntimeException("Not yet implemented!");
    }

}
