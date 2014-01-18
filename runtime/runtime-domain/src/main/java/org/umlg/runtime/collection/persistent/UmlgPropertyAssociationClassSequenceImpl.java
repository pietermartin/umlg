package org.umlg.runtime.collection.persistent;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import org.umlg.runtime.adaptor.GraphDb;
import org.umlg.runtime.collection.TinkerCollection;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.collection.UmlgPropertyAssociationClassSequence;
import org.umlg.runtime.domain.AssociationClassNode;
import org.umlg.runtime.domain.UmlgNode;

import java.util.Set;

/**
 * Date: 2013/06/18
 * Time: 5:59 PM
 */
public class UmlgPropertyAssociationClassSequenceImpl<E, AC extends AssociationClassNode> extends TinkerSequenceImpl<E> implements UmlgPropertyAssociationClassSequence<E, AC> {

    public UmlgPropertyAssociationClassSequenceImpl(UmlgNode owner, UmlgRuntimeProperty runtimeProperty, UmlgRuntimeProperty associationClassRuntimeProperty) {
        super(owner, runtimeProperty);
    }

    @Override
    public void add(int index, E e, AC associationClass) {
        super.add(index, e);
        associationClass.internalAdder(umlgRuntimeProperty, true, this.owner);
        associationClass.internalAdder(umlgRuntimeProperty, false, (UmlgNode) e);
        this.edge.setProperty(TinkerCollection.ASSOCIATION_CLASS_VERTEX_ID, associationClass.getId());
        this.edge.setProperty("className", associationClass.getClass().getName());
    }

    @Override
    public boolean add(E e, AC associationClass) {
        if (super.add(e)) {
            associationClass.internalAdder(umlgRuntimeProperty, true, this.owner);
            associationClass.internalAdder(umlgRuntimeProperty, false, (UmlgNode) e);
            this.edge.setProperty(TinkerCollection.ASSOCIATION_CLASS_VERTEX_ID, associationClass.getId());
            this.edge.setProperty("className", associationClass.getClass().getName());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean add(E e) {
        throw new RuntimeException("The standard add method can not be used on an edge that represents and association class. Please use the add(element, associationClass) method.");
    }

    @Override
    public boolean remove(Object o) {
        maybeLoad();
        Vertex v;
        if (o instanceof UmlgNode) {
            UmlgNode node = (UmlgNode) o;
            v = node.getVertex();
            removeEdge(v);
        } else if (o.getClass().isEnum()) {
            v = removeFromInternalMap(o);
            removeEdge(v);
            GraphDb.getDb().removeVertex(v);
        } else if (isOnePrimitive() || getDataTypeEnum() != null) {
            throw new IllegalStateException("one primitive or data type can not have an association class.");
        } else {
            v = removeFromInternalMap(o);
            removeEdge(v);
            GraphDb.getDb().removeVertex(v);
        }

        return super.remove(o);
    }

    private void removeEdge(Vertex v) {
        Set<Edge> edges = GraphDb.getDb().getEdgesBetween(this.vertex, v, this.getLabel());
        for (Edge edge : edges) {
            Vertex associationClassVertex = GraphDb.getDb().getVertex(edge.getProperty(TinkerCollection.ASSOCIATION_CLASS_VERTEX_ID));
            //The remove code will delete all in and out edges
            GraphDb.getDb().removeVertex(associationClassVertex);
        }
    }

}