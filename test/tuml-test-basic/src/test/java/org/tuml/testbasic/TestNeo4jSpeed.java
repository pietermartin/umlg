package org.tuml.testbasic;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Date: 2013/01/07
 * Time: 9:26 PM
 */
public class TestNeo4jSpeed {

    @Test
    public void testSpeed() throws IOException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        File f = new File("/tmp/orientdb-speed-test");
        FileUtils.deleteDirectory(f);
        Neo4jGraph graph = null;
        try {
            graph = new Neo4jGraph("local:/tmp/graph");
            for (int i = 0; i < 10000; i++) {
                Vertex v1 = graph.addVertex(null);
            }
        } finally {
            if (graph != null)
                graph.shutdown();
        }
        stopWatch.stop();
        System.out.println(stopWatch.toString());
    }
}
