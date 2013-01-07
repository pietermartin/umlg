package org.tuml.runtime.adaptor;

import com.tinkerpop.blueprints.*;

/**
 * Date: 2013/01/06
 * Time: 4:41 PM
 */
public abstract class BaseTumlGraph implements TumlGraph {

    private TransactionalGraph transactionalGraph;
    private IndexableGraph indexableGraph;
    private KeyIndexableGraph keyIndexableGraph;
    private Graph graph;

    public BaseTumlGraph(Graph graph) {
        this.graph = graph;
        this.transactionalGraph = (TransactionalGraph) graph;
        this.indexableGraph = (IndexableGraph) graph;
        this.keyIndexableGraph = (KeyIndexableGraph) graph;
    }

    @Override
    public void rollback() {
        this.transactionalGraph.rollback();
    }

    @Override
    public void commit() {
        this.transactionalGraph.commit();
//        TransactionManager tm = ((EmbeddedGraphDatabase) neo4jGraph.getRawGraph()).getTxManager();
//        try {
//            if (tm.getTransaction() != null) {
//                neo4jGraph.rollback();
//            }
//        } catch (SystemException e) {
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public void stopTransaction(Conclusion conclusion) {
        commit();
    }

    @Override
    public Features getFeatures() {
        return this.graph.getFeatures();
    }

    @Override
    public Vertex addVertex(Object id) {
        throw new RuntimeException("addVertex(Object id) is not supported, call addVertex(String className)");
    }

    @Override
    public Vertex getVertex(Object id) {
        return this.graph.getVertex(id);
    }

    @Override
    public void removeVertex(Vertex vertex) {
        TransactionThreadEntityVar.remove(vertex.getId().toString());
        this.graph.removeVertex(vertex);
    }

    @Override
    public Iterable<Vertex> getVertices() {
        return this.graph.getVertices();
    }

    @Override
    public Iterable<Vertex> getVertices(String key, Object value) {
        return this.graph.getVertices(key, value);
    }

    @Override
    public Edge addEdge(Object id, Vertex outVertex, Vertex inVertex, String label) {
        return this.graph.addEdge(id, outVertex, inVertex, label);
    }

    @Override
    public Edge getEdge(Object id) {
        return this.graph.getEdge(id);
    }

    @Override
    public void removeEdge(Edge edge) {
        this.graph.removeEdge(edge);
    }

    @Override
    public Iterable<Edge> getEdges() {
        return this.graph.getEdges();
    }

    @Override
    public Iterable<Edge> getEdges(String key, Object value) {
        return this.graph.getEdges(key, value);
    }

    @Override
    public void shutdown() {
        this.graph.shutdown();
    }


    @Override
    public void incrementTransactionCount() {
        getRoot().setProperty("transactionCount", (Integer) getRoot().getProperty("transactionCount") + 1);
    }

    @Override
    public long getTransactionCount() {
        return (Integer) getRoot().getProperty("transactionCount");
    }


    @Override
    public Vertex addVertex(String className) {
        Vertex v = this.graph.addVertex(null);
        if (className != null) {
            v.setProperty("className", className);
        }
        return v;
    }

    @Override
    public <T> T instantiateClassifier(Long id) {
        try {
            Vertex v = this.graph.getVertex(id);
            // TODO reimplement schemaHelper
            Class<?> c = Class.forName((String) v.getProperty("className"));
            // Class<?> c = schemaHelper.getClassNames().get((String)
            // v.getProperty("className"));
            return (T) c.getConstructor(Vertex.class).newInstance(v);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Index<? extends Element>> getIndices() {
        return this.indexableGraph.getIndices();
    }

    @Override
    public void dropIndex(String indexName) {
        this.indexableGraph.dropIndex(indexName);
    }

}
