package org.umlg.runtime.collection.persistent;

import com.tinkerpop.blueprints.Direction;
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
public class UmlgAssociationClassSequenceImpl<AssociationClassNode> extends TinkerSequenceImpl<AssociationClassNode> {

    public UmlgAssociationClassSequenceImpl(TumlNode owner, TumlRuntimeProperty runtimeProperty) {
        super(owner, runtimeProperty);
    }

    @Override
    protected void loadNode(Edge edgeToElement, Vertex vertexToLoad, boolean hyperVertexEdge) {
        AssociationClassNode node;
        try {

            //Get the edges between the vertexToLoad and the owner vertex.
            //Take the first one. If there
            Set<Edge> edges = GraphDb.getDb().getEdgesBetween(this.owner.getVertex(), vertexToLoad, getLabel());
            //Debug check
            if (edges.size() > 1) {
                throw new IllegalStateException("Only a bag can have multiple edges between vertixes!");
            }
            Vertex associationClassVertex = null;
            for (Edge edge : edges) {
                associationClassVertex = GraphDb.getDb().getVertex(edge.getProperty(TinkerCollection.ASSOCIATION_CLASS_VERTEX_ID));
            }

            Class<?> c;
            if (hyperVertexEdge) {
                c = Class.forName((String) associationClassVertex.getProperty("className"));
                //This is a debug check
                //TODO optimize
                Vertex debugVertex = edgeToElement.getVertex(Direction.IN);
                if (!debugVertex.equals(vertexToLoad)) {
                    throw new IllegalStateException("Vertexes should be the same, what is going on?");
                }
            } else {
                c = this.getClassToInstantiate(edgeToElement);
            }
            if (c.isEnum()) {
                Object value = vertexToLoad.getProperty("value");
                node = (AssociationClassNode) Enum.valueOf((Class<? extends Enum>) c, (String) value);
                this.internalVertexMap.put(value, vertexToLoad);
            } else if (TumlNode.class.isAssignableFrom(c)) {
                node = (AssociationClassNode) c.getConstructor(Vertex.class).newInstance(associationClassVertex);
            } else {
                Object value = vertexToLoad.getProperty("value");
                node = (AssociationClassNode) value;
                this.internalVertexMap.put(value, vertexToLoad);
            }
            this.getInternalList().add(node);
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
