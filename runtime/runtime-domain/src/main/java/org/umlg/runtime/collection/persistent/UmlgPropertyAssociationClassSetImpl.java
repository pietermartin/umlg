package org.umlg.runtime.collection.persistent;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.collection.UmlgCollection;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.collection.UmlgPropertyAssociationClassSet;
import org.umlg.runtime.domain.AssociationClassNode;
import org.umlg.runtime.domain.UmlgNode;

import java.util.Set;

/**
 * Date: 2013/06/18
 * Time: 5:59 PM
 */
public class UmlgPropertyAssociationClassSetImpl<E, AC extends AssociationClassNode> extends UmlgSetImpl<E> implements UmlgPropertyAssociationClassSet<E, AC> {

    public UmlgPropertyAssociationClassSetImpl(UmlgNode owner, UmlgRuntimeProperty runtimeProperty, UmlgRuntimeProperty associationClassRuntimeProperty) {
        super(owner, runtimeProperty);
    }

    @Override
    public boolean add(E e, AC associationClass) {
        //This is needed in handleInverseSide
        if (super.add(e)) {
            associationClass.internalAdder(this.umlgRuntimeProperty, true, this.owner);
            associationClass.internalAdder(this.umlgRuntimeProperty, false, (UmlgNode) e);
            this.edge.setProperty(UmlgCollection.ASSOCIATION_CLASS_VERTEX_ID, associationClass.getId().toString());
            this.edge.setProperty("className", associationClass.getClass().getName());
            associationClass.z_internalCopyOnePrimitivePropertiesToEdge(this.edge);
            associationClass.getVertex().setProperty(UmlgCollection.ASSOCIATION_CLASS_EDGE_ID, this.edge.getId().toString());
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
            UMLG.get().removeVertex(v);
        } else if (isOnePrimitive() || getDataTypeEnum() != null) {
            throw new IllegalStateException("one primitive or data type can not have an association class.");
        } else {
            v = removeFromInternalMap(o);
            removeEdge(v);
            UMLG.get().removeVertex(v);
        }

        return super.remove(o);
    }

    private void removeEdge(Vertex v) {
        Set<Edge> edges = UMLG.get().getEdgesBetween(this.vertex, v, this.getLabel());
        for (Edge edge : edges) {
            Vertex associationClassVertex = UMLG.get().getVertex(edge.getProperty(UmlgCollection.ASSOCIATION_CLASS_VERTEX_ID));
            //The remove code will delete all in and out edges
            UMLG.get().removeVertex(associationClassVertex);
        }
    }

}