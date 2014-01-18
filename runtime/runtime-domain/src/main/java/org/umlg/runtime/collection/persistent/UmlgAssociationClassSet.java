package org.umlg.runtime.collection.persistent;

import org.umlg.runtime.collection.UmlgSet;

/**
 * Date: 2013/07/27
 * Time: 7:49 PM
 */
public interface UmlgAssociationClassSet<E> extends UmlgSet<E> {
    boolean internalAdd(E node);
    boolean internalRemove(E node);
}
