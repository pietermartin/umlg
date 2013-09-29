package com.lambdazen.bitsy.wrapper;

import java.util.Iterator;
import java.util.Set;

import com.lambdazen.bitsy.*;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Features;
import com.tinkerpop.blueprints.GraphQuery;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Parameter;
import com.tinkerpop.blueprints.ThreadedTransactionalGraph;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.DefaultGraphQuery;
import com.tinkerpop.blueprints.util.wrappers.WrapperGraph;

public class BitsyAutoReloadingGraph implements WrapperGraph<BitsyGraph>, TransactionalGraph, KeyIndexableGraph, ThreadedTransactionalGraph {
    private BitsyGraph graph;

    public BitsyAutoReloadingGraph(BitsyGraph g) {
        this.graph = g;
    }
    
    @Override
    public BitsyGraph getBaseGraph() {
        return graph;
    }

    @Override
    public Features getFeatures() {
        return graph.getFeatures();
    }

    @Override
    public Vertex addVertex(Object id) {
        BitsyVertex base = (BitsyVertex)(graph.addVertex(id));
        
        if (base == null) {
            return null;
        } else {
            return new BitsyAutoReloadingVertex(graph, base);
        }
    }

    @Override
    public Vertex getVertex(Object id) {
        BitsyVertex base = (BitsyVertex)(graph.getVertex(id));

        if (base == null) {
            return null;
        } else {
            return new BitsyAutoReloadingVertex(graph, base);
        }
    }

    @Override
    public void removeVertex(Vertex vertex) {
        vertex.remove();
    }

    @Override
    public Iterable<Vertex> getVertices() {
        return new VertexIterable(graph, graph.getVertices());
    }

    @Override
    public Iterable<Vertex> getVertices(String key, Object value) {
        return new VertexIterable(graph, graph.getVertices(key, value));
    }

    @Override
    public Edge addEdge(Object id, Vertex outVertex, Vertex inVertex, String label) {
        BitsyEdge base = (BitsyEdge)(graph.addEdge(id, outVertex, inVertex, label));

        if (base == null) {
            return null;
        } else {
            return new BitsyAutoReloadingEdge(graph, base);
        }
    }

    @Override
    public Edge getEdge(Object id) {
        BitsyEdge base = (BitsyEdge)(graph.getEdge(id));
        
        if (base == null) {
            return null;
        } else {
            return new BitsyAutoReloadingEdge(graph, base);
        }
    }

    @Override
    public void removeEdge(Edge edge) {
        edge.remove();
    }

    @Override
    public Iterable<Edge> getEdges() {
        return new EdgeIterable(graph, graph.getEdges());
    }

    @Override
    public Iterable<Edge> getEdges(String key, Object value) {
        return new EdgeIterable(graph, graph.getEdges(key, value));
    }

    @Override
    public GraphQuery query() {
        return new DefaultGraphQuery(this);
    }

    @Override
    public <T extends Element> void dropKeyIndex(String key, Class<T> elementClass) {
        graph.dropKeyIndex(key, elementClass);
    }

    @Override
    public <T extends Element> void createKeyIndex(String key, Class<T> elementClass, Parameter... indexParameters) {
        graph.createKeyIndex(key, elementClass, indexParameters);
    }

    @Override
    public <T extends Element> Set<String> getIndexedKeys(Class<T> elementClass) {
        return graph.getIndexedKeys(elementClass);
    }

    @Override
    public void stopTransaction(Conclusion conclusion) {
        graph.stopTransaction(conclusion);
    }

    @Override
    public void shutdown() {
        graph.shutdown();
    }

    @Override
    public void commit() {
        graph.commit();
    }

    @Override
    public void rollback() {
        graph.rollback();
    }

    @Override
    public TransactionalGraph newTransaction() {
        return new BitsyAutoReloadingGraph((ThreadedBitsyGraph)(getBaseGraph().newTransaction()));
    }
    
    public String toString() {
        return "bitsyautoreloadinggraph[" + getBaseGraph().toString() + "]";
    }

    public static class VertexIterable implements Iterable<Vertex> {
        BitsyGraph graph;
        Iterable<Vertex> iter;

        public VertexIterable(BitsyGraph g, Iterable<Vertex> iter) {
            this.graph = g;
            this.iter = iter;
        }

        @Override
        public Iterator<Vertex> iterator() {
            return new VertexIterator(graph, iter.iterator());
        }
    }

    public static class VertexIterator implements Iterator<Vertex> {
        BitsyGraph graph;
        Iterator<Vertex> iter;

        public VertexIterator(BitsyGraph g, Iterator<Vertex> iter) {
            this.graph = g;
            this.iter = iter;
        }

        @Override
        public boolean hasNext() {
            return iter.hasNext();
        }

        @Override
        public Vertex next() {
            return new BitsyAutoReloadingVertex(graph, (BitsyVertex)(iter.next()));
        }

        @Override
        public void remove() {
            iter.remove();
        }
    }

    public static class EdgeIterable implements Iterable<Edge> {
        BitsyGraph graph;
        Iterable<Edge> iter;

        public EdgeIterable(BitsyGraph g, Iterable<Edge> iter) {
            this.graph = g;
            this.iter = iter;
        }

        @Override
        public Iterator<Edge> iterator() {
            return new EdgeIterator(graph, iter.iterator());
        }
    }

    public static class EdgeIterator implements Iterator<Edge> {
        BitsyGraph graph;
        Iterator<Edge> iter;

        public EdgeIterator(BitsyGraph g, Iterator<Edge> iter) {
            this.graph = g;
            this.iter = iter;
        }

        @Override
        public boolean hasNext() {
            return iter.hasNext();
        }

        @Override
        public Edge next() {
            return new BitsyAutoReloadingEdge(graph, (BitsyEdge)(iter.next()));
        }

        @Override
        public void remove() {
            iter.remove();
        }
    }

    public boolean isTransactionActive() {
        return getBaseGraph().isTransactionActive();
    }
}
