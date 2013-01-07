package org.tuml.orierntdb.test;

import com.orientechnologies.orient.core.db.graph.OGraphDatabase;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Date: 2013/01/07
 * Time: 8:53 PM
 */
public class OrientDbSpeedTest {

    @Test
    public void testSpeed() throws IOException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        File f = new File("/tmp/orientdb-speed-test");
        FileUtils.deleteDirectory(f);
        OrientGraph graph = null;
        try {
            graph = new OrientGraph("local:/tmp/graph");
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
