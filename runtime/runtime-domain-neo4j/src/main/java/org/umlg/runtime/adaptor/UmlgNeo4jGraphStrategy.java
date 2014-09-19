package org.umlg.runtime.adaptor;

import com.tinkerpop.gremlin.neo4j.structure.Neo4jVertex;
import com.tinkerpop.gremlin.structure.Graph;
import com.tinkerpop.gremlin.structure.Property;
import com.tinkerpop.gremlin.structure.Vertex;
import com.tinkerpop.gremlin.structure.strategy.*;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Date: 2014/06/26
 * Time: 12:15 PM
 */
public class UmlgNeo4jGraphStrategy implements GraphStrategy {

    @Override
    public UnaryOperator<Supplier<Void>> getRemoveVertexStrategy(final Strategy.Context<StrategyWrappedVertex> ctx) {
        if (ctx.getCurrent() instanceof StrategyWrappedVertex) {
            return (t) -> () -> {
                Vertex v = ((StrategyWrappedVertex) ctx.getCurrent()).getBaseVertex();
                v.bothE().forEach(e -> e.remove());
                getDeletionVertex(ctx.getBaseGraph()).addEdge(UmlgGraph.DELETION_VERTEX, v);
                v.properties().forEachRemaining(Property::remove);
                v.property("_deleted", true);
                ((Neo4jVertex)v).getBaseVertex().removeLabel(((Neo4jVertex)v).getBaseVertex().getLabels().iterator().next());
                return null;
            };
        } else {
            return UnaryOperator.identity();
        }
    }

    private Vertex getDeletionVertex(Graph g) {
        Vertex root = g.v(0L);
        if (root != null && root.outE(UmlgGraph.DELETED_VERTEX_EDGE).hasNext()) {
            return root.outE(UmlgGraph.DELETED_VERTEX_EDGE).next().inV().next();
        } else {
            throw new IllegalStateException("The root node or deletion vertex is not present. It must be created at graph initialization!");
        }
    }

}
