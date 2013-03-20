package org.tuml.runtime.adaptor;

import com.tinkerpop.blueprints.*;

import java.io.Serializable;
import java.util.Set;

public interface TumlGraph extends TransactionalGraph, IndexableGraph, Serializable {
    static final String ROOT_CLASS_NAME = "org.tuml.root.Root";

    void incrementTransactionCount();

    long getTransactionCount();

    Vertex getRoot();

    Vertex addVertex(String className);

    Set<Edge> getEdgesBetween(Vertex v1, Vertex v2, String... labels);

    void addRoot();

    long countVertices();

    long countEdges();

    void registerListeners();

    //	<T> List<T> query(Class<?> className, int first, int pageSize);
    <T> T instantiateClassifier(Long id);

    void resume(TransactionIdentifier t);

    TransactionIdentifier suspend();

    void setRollbackOnly();

    <T extends Element> TumlTinkerIndex<T> createIndex(String indexName, Class<T> indexClass);

    <T extends Element> TumlTinkerIndex<T> getIndex(String indexName, Class<T> indexClass);

    boolean hasEdgeBeenDeleted(Edge edge);

    //TODO devise some timeout strategy
    void acquireWriteLock(Vertex vertex);

    void clearTxThreadVar();

//    void createUniqueVertexIndex();

    boolean isTransactionActive();

    void addDeletionNode();
}