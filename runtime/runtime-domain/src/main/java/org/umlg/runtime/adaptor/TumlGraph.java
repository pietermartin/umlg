package org.umlg.runtime.adaptor;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

import java.util.Set;

public interface TumlGraph extends Graph {
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

    void registerListeners();

    <T> T instantiateClassifier(Long id);

    boolean hasEdgeBeenDeleted(Edge edge);

    void clearTxThreadVar();

    void clearThreadVars();

    void addDeletionNode();

    String executeQuery(TumlQueryEnum tumlQueryEnum, Long contextId, String query);

}