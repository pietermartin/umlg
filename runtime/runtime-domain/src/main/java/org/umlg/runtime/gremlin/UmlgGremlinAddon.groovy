package org.umlg.runtime.gremlin

import com.tinkerpop.blueprints.Edge
import com.tinkerpop.blueprints.Graph
import com.tinkerpop.blueprints.Predicate
import com.tinkerpop.blueprints.Vertex
import com.tinkerpop.gremlin.Tokens
import com.tinkerpop.gremlin.groovy.Gremlin
import com.tinkerpop.pipes.Pipe

/**
 * Created by pieter on 2014/03/19.
 */
class UmlgGremlinAddon {

    public static void defineUmlgSteps(final Graph graph) {
        Gremlin.defineStep("hasAC", [Pipe, Edge],
                {
                    final String key, Tokens.T compareToken, Object value ->
                        Predicate predicate = Tokens.mapPredicate(compareToken);
                        return new AssociationClassPropertyFilter(graph, key, predicate, value);
                });

        Gremlin.defineStep("V", [Pipe, Vertex],
                {
                    final String key, Tokens.T compareToken, Object value ->
                        Predicate predicate = Tokens.mapPredicate(compareToken);
                        return new AssociationClassPropertyFilter(graph, key, predicate, value);
                });

    }

}