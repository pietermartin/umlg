package org.umlg.runtime.adaptor;

import org.apache.tinkerpop.gremlin.neo4j.structure.Neo4jVertex;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.strategy.GraphStrategy;
import org.apache.tinkerpop.gremlin.structure.strategy.StrategyContext;
import org.apache.tinkerpop.gremlin.structure.strategy.StrategyVertex;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Date: 2014/06/26
 * Time: 12:15 PM
 */
public class UmlgNeo4jGraphStrategy implements GraphStrategy {

    @Override
    public UnaryOperator<Supplier<Void>> getRemoveVertexStrategy(final StrategyContext<StrategyVertex> ctx, final GraphStrategy composingStrategy) {
        if (ctx.getCurrent() instanceof StrategyVertex) {
            return (t) -> () -> {
                Vertex v = (ctx.getCurrent()).getBaseVertex();
                v.edges(Direction.BOTH).forEachRemaining(e -> e.remove());
                getDeletionVertex(ctx.getStrategyGraph()).addEdge(UmlgGraph.DELETION_VERTEX, v);
                v.properties().forEachRemaining(Property::remove);
                v.property("_deleted", true);
                ((Neo4jVertex) v).getBaseVertex().removeLabel(((Neo4jVertex) v).getBaseVertex().getLabels().iterator().next());
                return null;
            };
        } else {
            return UnaryOperator.identity();
        }
    }

    private Vertex getDeletionVertex(Graph g) {
        Vertex root = g.traversal().V(0L).next();
        if (root != null && root.edges(Direction.OUT, UmlgGraph.DELETED_VERTEX_EDGE).hasNext()) {
            return root.edges(Direction.OUT, UmlgGraph.DELETED_VERTEX_EDGE).next().vertices(Direction.IN).next();
        } else {
            throw new IllegalStateException("The root node or deletion vertex is not present. It must be created at graph initialization!");
        }
    }

}
