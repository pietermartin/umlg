package org.tuml.runtime.gremlin;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.wrappers.readonly.ReadOnlyGraph;
import com.tinkerpop.gremlin.groovy.Gremlin;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.iterators.SingleIterator;
import org.tuml.runtime.adaptor.GraphDb;

import java.util.Map;

/**
 * Date: 2013/06/09
 * Time: 12:24 PM
 */
public class GremlinExecutor {

    public static String executeGremlinQuery(Long contextId, String gremlin) {
        StringBuilder result = new StringBuilder();
        Graph graph = new ReadOnlyGraph(GraphDb.getDb());
        Pipe pipe = Gremlin.compile("_()." + gremlin);
        pipe.setStarts(new SingleIterator<Vertex>(graph.getVertex(contextId)));
        for(Object o : pipe) {
            if (o instanceof String) {
                result.append((String) o);
            } else if (o instanceof Map) {
                Map map = (Map)o;
                for (Object key : map.keySet()) {
                    result.append(key);
                    result.append(": ");
                    result.append(map.get(key));
                    result.append("\\n");
                }
            } else {
                result.append(o.toString());
            }
        }
        return result.toString();
    }

}
