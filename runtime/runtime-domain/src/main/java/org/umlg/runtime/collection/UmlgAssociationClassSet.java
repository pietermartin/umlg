package org.umlg.runtime.collection;

import org.umlg.runtime.domain.TumlNode;

/**
 * Date: 2013/06/18
 * Time: 6:00 PM
 */
public interface UmlgAssociationClassSet<E, AC extends TumlNode> extends TinkerSet<E> {

    boolean add(E e, AC associationClass);
}
