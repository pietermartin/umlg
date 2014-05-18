package org.umlg.runtime.gremlin;

import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.util.StringFactory;
import com.tinkerpop.blueprints.util.wrappers.WrapperGraph;
import com.tinkerpop.blueprints.util.wrappers.readonly.ReadOnlyTokens;

/**
 * Date: 2014/03/21
 * Time: 2:48 PM
 */
public class UmlgGremlinReadOnlyGraph<T extends Graph> implements Graph, WrapperGraph<T> {

    protected final T baseGraph;
    private final Features features;

    public UmlgGremlinReadOnlyGraph(final T baseGraph) {
        this.baseGraph = baseGraph;
        this.features = this.baseGraph.getFeatures().copyFeatures();
        this.features.isWrapper = true;
    }

    /**
     * @throws UnsupportedOperationException
     */
    public void removeVertex(final Vertex vertex) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(ReadOnlyTokens.MUTATE_ERROR_MESSAGE);
    }

    /**
     * @throws UnsupportedOperationException
     */
    public Edge addEdge(final Object id, final Vertex outVertex, final Vertex inVertex, final String label) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(ReadOnlyTokens.MUTATE_ERROR_MESSAGE);
    }

    public Vertex getVertex(final Object id) {
        final Vertex vertex = this.baseGraph.getVertex(id);
        if (null == vertex)
            return null;
        else
            return new UmlgGremlinReadOnlyVertex(vertex);
    }

    /**
     * @throws UnsupportedOperationException
     */
    public void removeEdge(final Edge edge) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(ReadOnlyTokens.MUTATE_ERROR_MESSAGE);
    }

    public Iterable<Edge> getEdges() {
        return new UmlgGremlinReadOnlyEdgeIterable(this.baseGraph.getEdges());
    }

    public Iterable<Edge> getEdges(final String key, final Object value) {
        return new UmlgGremlinReadOnlyEdgeIterable(this.baseGraph.getEdges(key, value));
    }

    public Edge getEdge(final Object id) {
        final Edge edge = this.baseGraph.getEdge(id);
        if (null == edge)
            return null;
        else
            return new UmlgGremlinReadOnlyEdge(edge);
    }

    public Iterable<Vertex> getVertices() {
        return new UmlgGremlinReadOnlyVertexIterable(this.baseGraph.getVertices());
    }

    public Iterable<Vertex> getVertices(final String key, final Object value) {
        return new UmlgGremlinReadOnlyVertexIterable(this.baseGraph.getVertices(key, value));
    }

    /**
     * @throws UnsupportedOperationException
     */
    public Vertex addVertex(final Object id) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(ReadOnlyTokens.MUTATE_ERROR_MESSAGE);
    }

    /**
     * @throws UnsupportedOperationException
     */
    public void shutdown() throws UnsupportedOperationException {
        throw new UnsupportedOperationException(ReadOnlyTokens.MUTATE_ERROR_MESSAGE);
    }

    public String toString() {
        return StringFactory.graphString(this, this.baseGraph.toString());
    }

    @Override
    public T getBaseGraph() {
        return this.baseGraph;
    }

    public GraphQuery query() {
        return new UmlgReadOnlyGraphQuery(this.baseGraph);
//        {
//            @Override
//            public Iterable<Edge> edges() {
//                return new UmlgGremlinReadOnlyEdgeIterable(this.query.edges());
//            }
//
//            @Override
//            public Iterable<Vertex> vertices() {
//                return new UmlgGremlinReadOnlyVertexIterable(this.query.vertices());
//            }
//        };
    }

    public Features getFeatures() {
        return this.features;
    }

}
