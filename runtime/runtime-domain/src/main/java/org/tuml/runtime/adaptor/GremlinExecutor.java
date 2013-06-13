package org.tuml.runtime.adaptor;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.wrappers.readonly.ReadOnlyGraph;
import com.tinkerpop.gremlin.groovy.Gremlin;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.Pipeline;
import com.tinkerpop.pipes.util.iterators.SingleIterator;
import org.apache.commons.lang.time.StopWatch;

/**
 * Date: 2013/06/09
 * Time: 8:34 PM
 */
public class GremlinExecutor {

    public static String executeGremlinQuery(Long contextId, String gremlin) {
        StringBuilder result = new StringBuilder();
        Graph graph = new ReadOnlyGraph(GraphDb.getDb());
        Pipe pipe = Gremlin.compile("_()." + gremlin);
        GremlinToStringPipe<String> toStringPipe = new GremlinToStringPipe<String>();
        Pipeline<Vertex,String> pipeline = new Pipeline<Vertex,String>(pipe, toStringPipe);
        pipeline.setStarts(new SingleIterator<Vertex>(graph.getVertex(contextId)));

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        while (pipeline.hasNext()) {
            result.append(pipeline.next());
            result.append("\n");
        }
        stopWatch.stop();
        result.append("Time taken: ");
        result.append(stopWatch.toString());
        return result.toString();
    }
}
