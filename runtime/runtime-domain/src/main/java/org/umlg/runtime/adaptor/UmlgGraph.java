package org.umlg.runtime.adaptor;

import com.tinkerpop.blueprints.*;

import java.util.Set;

public interface UmlgGraph extends TransactionalGraph, KeyIndexableGraph {
    static final String ROOT_VERTEX = "UmlgRootVertex";
    static final String DELETION_VERTEX = "deletionVertex";
    static final String DELETED_VERTEX_EDGE = "deletedVertexEdgeToRoot";
    static final String ROOT_CLASS_NAME = "org.umlg.root.Root";

    /**
     * Adds in a singleton super node. Represents the application/model.
     * Attached to the root node is each concrete classes meta class.
     */
    void addRoot();

    /**
     * @return The singleton root node of the application/model
     */
    Vertex getRoot();

    Vertex addVertex(String className);

    Set<Edge> getEdgesBetween(Vertex v1, Vertex v2, String... labels);

    long countVertices();

    long countEdges();

    <T> T instantiateClassifier(Object id);

    boolean hasEdgeBeenDeleted(Edge edge);

    boolean isTransactionActive();

    /**
     * To be called at the end of a threads interaction with the graph.
     * This is useful for clearing thread locals.
     * Blueprints implementation have different threading models.
     * For OrientDb this is where the graph will be shutdown.
     */
    void afterThreadContext();

    /**
     * For blueprint's implementation that recycle ids, vertexes are not removed but instead attached to a deletion node.
     * The deletion node is attached to the root node.
     */
    void addDeletionNode();

    String executeQuery(UmlgQueryEnum umlgQueryEnum, Object contextId, String query);

    void drop();

    //Used for auditing
    void incrementTransactionCount();

    //Used for auditing
    long getTransactionCount();

}