package org.umlg.runtime.adaptor;

import com.tinkerpop.blueprints.*;

import java.util.Set;

public interface UmlgGraph extends TransactionalGraph, KeyIndexableGraph {
    static final String ROOT_VERTEX = "UmlgRootVertex";
    static final String DELETION_VERTEX = "deletionVertex";
    static final String DELETED_VERTEX_EDGE = "deletedVertexEdgeToRoot";
    static final String ROOT_CLASS_NAME = "org.umlg.root.Root";

    //Used for auditing
    void incrementTransactionCount();

    //Used for auditing
    long getTransactionCount();

    void addRoot();

    Vertex getRoot();

    Vertex addVertex(String className);

    Set<Edge> getEdgesBetween(Vertex v1, Vertex v2, String... labels);

    long countVertices();

    long countEdges();

    <T> T instantiateClassifier(Object id);

    boolean hasEdgeBeenDeleted(Edge edge);

    void clearTxThreadVar();

    void clearThreadVars();

    void addDeletionNode();

    String executeQuery(UmlgQueryEnum umlgQueryEnum, Object contextId, String query);

    void drop();

}