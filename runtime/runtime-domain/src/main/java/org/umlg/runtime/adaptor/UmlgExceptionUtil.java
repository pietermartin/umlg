package org.umlg.runtime.adaptor;

/**
 * Date: 2013/02/08
 * Time: 8:30 PM
 */
public interface UmlgExceptionUtil {
    RuntimeException handle(Exception e);
    boolean isNodeNotFoundException(Exception e);
}
