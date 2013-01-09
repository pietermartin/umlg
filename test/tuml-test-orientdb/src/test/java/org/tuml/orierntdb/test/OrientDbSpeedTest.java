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

    @Test
    public void testRaw() throws IOException {
        File f = new File("/tmp/orientdb-raw-speed-test");
        FileUtils.deleteDirectory(f);
        OGraphDatabase database = new OGraphDatabase("local:/tmp/orientdb-raw-speed-test");
        database.create();

        ODocument rootNode = database.createVertex().field("id", 0);
        ODocument currentNode = rootNode;

        for (int i = 1; i < 1000; ++i) {
            ODocument newNode = database.createVertex().field("id", i);
            database.createEdge( currentNode, newNode);
            currentNode = newNode;
        }
        database.setRoot("graph", rootNode);

        database.close();
    }

}
