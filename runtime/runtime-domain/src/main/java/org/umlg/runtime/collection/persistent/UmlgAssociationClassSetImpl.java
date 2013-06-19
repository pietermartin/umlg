package org.umlg.runtime.collection.persistent;

import org.umlg.runtime.collection.UmlgAssociationClassSet;
import org.umlg.runtime.collection.TumlRuntimeProperty;
import org.umlg.runtime.domain.TumlNode;

/**
 * Date: 2013/06/18
 * Time: 5:59 PM
 */
public class UmlgAssociationClassSetImpl<E, AC extends TumlNode> extends TinkerSetImpl<E> implements UmlgAssociationClassSet<E, AC> {

    public UmlgAssociationClassSetImpl(TumlNode owner, TumlRuntimeProperty runtimeProperty) {
        super(owner, runtimeProperty);
    }

    @Override
    public boolean add(E e, AC associationClass) {
        super.add(e);
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean add(E e) {
        throw new RuntimeException("The standard add method can not be used on an edge that represents and association class. Please use the add(element, associationClass) method.");
    }

}
