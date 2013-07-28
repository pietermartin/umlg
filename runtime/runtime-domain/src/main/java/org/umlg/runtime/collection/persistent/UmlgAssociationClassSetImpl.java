package org.umlg.runtime.collection.persistent;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import org.umlg.runtime.adaptor.GraphDb;
import org.umlg.runtime.collection.TinkerCollection;
import org.umlg.runtime.collection.TumlRuntimeProperty;
import org.umlg.runtime.domain.AssociationClassNode;
import org.umlg.runtime.domain.TumlNode;

/**
 * This set is used for navigating from a class to its related association classes.
 * The association class's vertex id is stored on edge to its member end.
 * Date: 2013/06/22
 * Time: 10:08 AM
 */
public class UmlgAssociationClassSetImpl<AssociationClassNode> extends TinkerSetImpl<AssociationClassNode> implements UmlgAssociationClassSet<AssociationClassNode> {

    public UmlgAssociationClassSetImpl(TumlNode owner, TumlRuntimeProperty runtimeProperty) {
        super(owner, runtimeProperty);
    }

    @Override
    protected Class<?> getClassToInstantiate(Edge edge) {
        try {
            Vertex associationClassVertex = GraphDb.getDb().getVertex(edge.getProperty(TinkerCollection.ASSOCIATION_CLASS_VERTEX_ID));
            return Class.forName((String) associationClassVertex.getProperty("className"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Vertex getVertexForDirection(Edge edge) {
        Vertex associationClassVertex = GraphDb.getDb().getVertex(edge.getProperty(TinkerCollection.ASSOCIATION_CLASS_VERTEX_ID));
        return associationClassVertex;
    }

    @Override
    public boolean add(AssociationClassNode node) {
        throw new RuntimeException("The collection to an association class is immutable!");
    }

    @Override
    public boolean remove(Object o) {
        throw new RuntimeException("The collection to an association class is immutable!");
    }

    /**
     * This add does not created an edge.
     * It is used internally to keep the object in memory.
     * @return
     */
    @Override
    public boolean internalAdd(AssociationClassNode node) {
        return this.internalCollection.add(node);
    }

    /**
     * This does not remove any edges. It only removes the object from memory
     * @param node
     * @return
     */
    @Override
    public boolean internalRemove(AssociationClassNode node) {
        return this.internalCollection.add(node);
    }

}
