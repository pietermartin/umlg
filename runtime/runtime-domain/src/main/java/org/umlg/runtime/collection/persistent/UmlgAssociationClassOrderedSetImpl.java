package org.umlg.runtime.collection.persistent;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import org.umlg.runtime.adaptor.GraphDb;
import org.umlg.runtime.collection.TinkerCollection;
import org.umlg.runtime.collection.TumlRuntimeProperty;
import org.umlg.runtime.domain.TumlNode;

import java.util.Set;

/**
 * This set is used for navigating from a class to its related association classes.
 * Date: 2013/06/22
 * Time: 10:08 AM
 */
public class UmlgAssociationClassOrderedSetImpl<AssociationClassNode> extends TinkerOrderedSetImpl<AssociationClassNode> {

    public UmlgAssociationClassOrderedSetImpl(TumlNode owner, TumlRuntimeProperty runtimeProperty) {
        super(owner, runtimeProperty);
    }

    @Override
    protected void loadNode(Edge edgeToFirstElement, Vertex vertex) {
        AssociationClassNode node;
        try {
            //Get the edges between the vertexToLoad and the owner vertex.
            //Take the first one. If there
            Set<Edge> edges = GraphDb.getDb().getEdgesBetween(this.owner.getVertex(), vertex, getLabel());
            //Debug check
            if (edges.size() > 1) {
                throw new IllegalStateException("Only a bag can have multiple edges between vertices!");
            }
            Vertex associationClassVertex = null;
            for (Edge edge : edges) {
                associationClassVertex = GraphDb.getDb().getVertex(edge.getProperty(TinkerCollection.ASSOCIATION_CLASS_VERTEX_ID));
            }
            if (associationClassVertex == null) {
                throw new IllegalStateException("Can not find associationClassVertex, this is a bug!");
            }

            Class<?> c = Class.forName((String) associationClassVertex.getProperty("className"));
            if (TumlNode.class.isAssignableFrom(c)) {
                node = (AssociationClassNode) c.getConstructor(Vertex.class).newInstance(associationClassVertex);
            } else if (c.isEnum()) {
                Object value = associationClassVertex.getProperty("value");
                node = (AssociationClassNode) Enum.valueOf((Class<? extends Enum>) c, (String) value);
                this.internalVertexMap.put(value, associationClassVertex);
            } else {
                Object value = associationClassVertex.getProperty("value");
                node = (AssociationClassNode) value;
                this.internalVertexMap.put(value, associationClassVertex);
            }
            this.getInternalListOrderedSet().add(node);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean add(AssociationClassNode e) {
        throw new RuntimeException("The collection to an association class is immutable!");
    }

    @Override
    public boolean remove(Object o) {
        throw new RuntimeException("The collection to an association class is immutable!");
    }

}
