package org.umlg.runtime.gremlin;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.VertexQuery;
import com.tinkerpop.blueprints.util.wrappers.WrapperVertexQuery;
import com.tinkerpop.blueprints.util.wrappers.readonly.ReadOnlyTokens;

/**
 * Date: 2014/03/21
 * Time: 3:05 PM
 */
public class UmlgGremlinReadOnlyVertex extends UmlgGremlinReadOnlyElement implements Vertex {

    public UmlgGremlinReadOnlyVertex(final Vertex baseVertex) {
        super(baseVertex);
    }

    public Iterable<Edge> getEdges(final Direction direction, final String... labels) {
        return new UmlgGremlinReadOnlyEdgeIterable(((Vertex) this.baseElement).getEdges(direction, labels));
    }

    public Iterable<Vertex> getVertices(final Direction direction, final String... labels) {
        return new UmlgGremlinReadOnlyVertexIterable(((Vertex) this.baseElement).getVertices(direction, labels));
    }

    public Edge addEdge(final String label, final Vertex vertex) {
        throw new UnsupportedOperationException(ReadOnlyTokens.MUTATE_ERROR_MESSAGE);
    }

    public VertexQuery query() {
        return new WrapperVertexQuery(((Vertex) this.baseElement).query()) {
            @Override
            public Iterable<Vertex> vertices() {
                return new UmlgGremlinReadOnlyVertexIterable(this.query.vertices());
            }

            @Override
            public Iterable<Edge> edges() {
                return new UmlgGremlinReadOnlyEdgeIterable(this.query.edges());
            }
        };
    }

}
