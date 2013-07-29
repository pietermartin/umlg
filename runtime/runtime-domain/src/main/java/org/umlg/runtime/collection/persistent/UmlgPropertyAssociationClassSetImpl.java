package org.umlg.runtime.collection.persistent;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import org.umlg.runtime.adaptor.GraphDb;
import org.umlg.runtime.collection.TinkerCollection;
import org.umlg.runtime.collection.TumlRuntimeProperty;
import org.umlg.runtime.collection.UmlgPropertyAssociationClassSet;
import org.umlg.runtime.domain.AssociationClassNode;
import org.umlg.runtime.domain.TumlNode;

import java.util.Set;

/**
 * Date: 2013/06/18
 * Time: 5:59 PM
 */
public class UmlgPropertyAssociationClassSetImpl<E, AC extends AssociationClassNode> extends TinkerSetImpl<E> implements UmlgPropertyAssociationClassSet<E, AC> {

    private TumlRuntimeProperty associationClassRuntimeProperty;
    private AC associationClass;

    public UmlgPropertyAssociationClassSetImpl(TumlNode owner, TumlRuntimeProperty runtimeProperty, TumlRuntimeProperty associationClassRuntimeProperty) {
        super(owner, runtimeProperty);
        this.associationClassRuntimeProperty = associationClassRuntimeProperty;
    }

    @Override
    public boolean add(E e, AC associationClass) {
        //This is needed in handleInverseSide
        this.associationClass = associationClass;
        if (super.add(e)) {
            this.associationClass = null;
            associationClass.internalAdder(this.tumlRuntimeProperty, true, this.owner);
            associationClass.internalAdder(this.tumlRuntimeProperty, false, (TumlNode) e);
            this.edge.setProperty(TinkerCollection.ASSOCIATION_CLASS_VERTEX_ID, associationClass.getId());
            this.edge.setProperty("className", associationClass.getClass().getName());
            return true;
        } else {
            this.associationClass = null;
            return false;
        }
    }

    @Override
    protected void handleInverseSide(TumlNode node, TumlRuntimeProperty tumlRuntimeProperty, boolean b, TumlNode owner) {
        super.handleInverseSide(node, tumlRuntimeProperty, b, owner);
        this.owner.inverseAdder(this.associationClassRuntimeProperty, true, this.associationClass);
    }

    @Override
    public boolean add(E e) {
        throw new RuntimeException("The standard add method can not be used on an edge that represents and association class. Please use the add(element, associationClass) method.");
    }

    @Override
    public boolean remove(Object o) {
        maybeLoad();
        Vertex v;
        if (o instanceof TumlNode) {
            TumlNode node = (TumlNode) o;
            v = node.getVertex();
            removeEdge(v);
        } else if (o.getClass().isEnum()) {
            v = removeFromInternalMap(((Enum<?>) o).name());
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