package org.umlg.runtime.collection.persistent;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.collection.UmlgCollection;
import org.umlg.runtime.collection.UmlgPropertyAssociationClassOrderedSet;
import org.umlg.runtime.collection.UmlgRuntimeProperty;
import org.umlg.runtime.domain.AssociationClassNode;
import org.umlg.runtime.domain.UmlgNode;

import java.util.Set;

/**
 * Date: 2013/06/18
 * Time: 5:59 PM
 */
public class UmlgPropertyAssociationClassOrderedSetImpl<E, AC extends AssociationClassNode> extends UmlgOrderedSetImpl<E> implements UmlgPropertyAssociationClassOrderedSet<E, AC> {

    public UmlgPropertyAssociationClassOrderedSetImpl(UmlgNode owner, PropertyTree propertyTree, boolean loaded1, PropertyTree associationClassRuntimeProperty, boolean loaded2) {
        super(owner, propertyTree);
    }

    public UmlgPropertyAssociationClassOrderedSetImpl(UmlgNode owner, UmlgRuntimeProperty runtimeProperty, UmlgRuntimeProperty associationClassRuntimeProperty) {
        super(owner, runtimeProperty);
    }

    @Override
    public boolean add(E e, AC associationClass) {
        if (super.add(e)) {
            associationClass.internalAdder(umlgRuntimeProperty, true, this.owner);
            associationClass.internalAdder(umlgRuntimeProperty, false, (UmlgNode) e);
            this.edge.property(UmlgCollection.ASSOCIATION_CLASS_VERTEX_ID, associationClass.getId().toString());
            this.edge.property("className", associationClass.getClass().getName());
            associationClass.z_internalCopyOnePrimitivePropertiesToEdge(this.edge);
            associationClass.getVertex().property(UmlgCollection.ASSOCIATION_CLASS_EDGE_ID, this.edge.id().toString());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void add(int index, E e, AC associationClass) {
        super.add(index, e);
        associationClass.internalAdder(umlgRuntimeProperty, true, this.owner);
        associationClass.internalAdder(umlgRuntimeProperty, false, (UmlgNode) e);
        this.edge.property(UmlgCollection.ASSOCIATION_CLASS_VERTEX_ID, associationClass.getId().toString());
        this.edge.property("className", associationClass.getClass().getName());
        associationClass.z_internalCopyOnePrimitivePropertiesToEdge(this.edge);
        associationClass.getVertex().property(UmlgCollection.ASSOCIATION_CLASS_EDGE_ID, this.edge.id().toString());
    }

    @Override
    public boolean add(E e) {
        throw new RuntimeException("The standard add method can not be used on an edge that represents and association class. Please use the add(element, associationClass) method.");
    }

    @Override
    public void move(int index, E e, AC associationClass) {
        maybeLoad();
        Vertex v;
        if (e instanceof UmlgNode) {
            UmlgNode node = (UmlgNode) e;
            v = node.getVertex();
        } else if (e.getClass().isEnum()) {
            throw new RuntimeException();
        } else if (isOnePrimitive() || getDataTypeEnum() != null) {
            throw new IllegalStateException("one primitive or data type can not have an association class.");
        } else {
            throw new RuntimeException();
        }
        //remove the edge
        super.remove(e);
        //add a new the edge
        add(index, e);
        //set association class vertex id on new edge
        this.edge.property(UmlgCollection.ASSOCIATION_CLASS_VERTEX_ID, associationClass.getId().toString());
        this.edge.property("className", associationClass.getClass().getName());
    }

    //This needs to be overridden because of add that may not be called as it is from the base class
    @Override
    public void add(int indexOf, E e) {
        maybeLoad();
        if (indexOf == getInternalList().size()) {
            super.add(e);
        } else if (indexOf > getInternalList().size()) {
            throw new IndexOutOfBoundsException("Index: " + indexOf + ", Size: " + getInternalList().size());
        } else if (!this.getInternalListOrderedSet().contains(e)) {
            super.add(indexOf, e);
        }
    }

    @Override
    public boolean remove(Object o) {
        maybeLoad();
        Vertex v;
        if (o instanceof UmlgNode) {
            UmlgNode node = (UmlgNode) o;
            v = node.getVertex();
            removeAssociationClassVertex(v);
        } else if (o.getClass().isEnum()) {
            throw new RuntimeException();
        } else if (isOnePrimitive() || getDataTypeEnum() != null) {
            throw new IllegalStateException("one primitive or data type can not have an association class.");
        } else {
            throw new RuntimeException();
        }

        return super.remove(o);
    }

    private void removeAssociationClassVertex(Vertex v) {
        Set<Edge> edges = UMLG.get().getEdgesBetween(this.vertex, v, this.getLabel());
        for (Edge edge : edges) {
            String value = edge.value(UmlgCollection.ASSOCIATION_CLASS_VERTEX_ID);
            Vertex associationClassVertex = UMLG.get().traversal().V(value).next();
            //The remove code will delete all in and out edges
            associationClassVertex.remove();
        }
    }

}