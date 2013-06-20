package org.umlg.runtime.collection.persistent;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import org.umlg.runtime.collection.TumlRuntimeProperty;
import org.umlg.runtime.collection.UmlgAssociationClassSet;
import org.umlg.runtime.domain.AssociationClassNode;
import org.umlg.runtime.domain.TumlNode;

import java.util.Iterator;

/**
 * Date: 2013/06/18
 * Time: 5:59 PM
 */
public class UmlgAssociationClassSetImpl<E, AC extends AssociationClassNode> extends TinkerSetImpl<E> implements UmlgAssociationClassSet<E, AC> {

    public UmlgAssociationClassSetImpl(TumlNode owner, TumlRuntimeProperty runtimeProperty) {
        super(owner, runtimeProperty);
    }

    @Override
    public boolean add(E e, AC associationClass) {
        super.add(e);
        associationClass.internalAdder(tumlRuntimeProperty, true, this.owner);
        associationClass.internalAdder(tumlRuntimeProperty, false, (TumlNode)e);
        return true;
    }

    @Override
    public boolean add(E e) {
        throw new RuntimeException("The standard add method can not be used on an edge that represents and association class. Please use the add(element, associationClass) method.");
    }

    @Override
    protected Iterator<Edge> getEdges() {
        if (this.isControllingSide()) {
            return this.vertex.getEdges(Direction.OUT, this.getLabel()).iterator();
        } else {
            return this.vertex.getEdges(Direction.IN, this.getLabel()).iterator();
        }
    }

}
