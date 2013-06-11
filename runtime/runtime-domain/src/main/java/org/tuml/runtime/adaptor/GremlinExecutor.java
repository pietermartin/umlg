package org.tuml.runtime.adaptor;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.wrappers.readonly.ReadOnlyGraph;
import com.tinkerpop.gremlin.groovy.Gremlin;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.iterators.SingleIterator;

/**
 * Date: 2013/06/09
 * Time: 8:34 PM
 */
public class GremlinExecutor {

    public static String executeGremlinQuery(Long contextId, String gremlin) {
        StringBuilder result = new StringBuilder();
        Graph graph = new ReadOnlyGraph(GraphDb.getDb());
        Pipe pipe = Gremlin.compile("_()." + gremlin);
        pipe.setStarts(new SingleIterator<Vertex>(graph.getVertex(contextId)));
        GremlinToStringPipe gremlinToStringPipe = new GremlinToStringPipe();
        gremlinToStringPipe.setStarts(pipe);
        for(String s : gremlinToStringPipe) {
            result.append(s);
        }
        return result.toString();
    }
}
