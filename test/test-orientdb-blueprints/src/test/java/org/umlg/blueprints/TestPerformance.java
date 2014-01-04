package org.umlg.blueprints;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Date: 2013/12/30
 * Time: 9:49 PM
 */
public class TestPerformance {

    @Test
    public void testSpeedDude() throws IOException {
        String url = "/tmp/test-orientdb-blueprints";
        File dir = new File(url);
        FileUtils.deleteDirectory(dir);
        File f = new File(url);
        TransactionalGraph g = new OrientGraph("local:" + f.getAbsolutePath());
        try {

            int NUMBER_TO_ITER = 10000000;

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            Vertex one = g.addVertex(null);
            one.setProperty("one", "1");
            long previousSplitTime = 0;
            for (int i = 0; i < NUMBER_TO_ITER; i++) {
                Vertex many = g.addVertex(null);
                many.setProperty("many", "2");
                g.addEdge(null, one, many, "toMany");

                if (i != 0 && i % 100000 == 0) {
                    stopWatch.split();
                    long splitTime = stopWatch.getSplitTime();
                    System.out.println(i + " " + stopWatch.toString() + " 100000 in " + (splitTime - previousSplitTime));
                    previousSplitTime = stopWatch.getSplitTime();
                    g.commit();
                }
            }
            g.commit();
            stopWatch.stop();
            System.out.println("write " + NUMBER_TO_ITER + " = " + stopWatch.toString());

            stopWatch.reset();
            stopWatch.start();
            int count = 1;
            Vertex startV = g.getVertex(one.getId());
            for (Vertex v : startV.getVertices(Direction.OUT)) {
                v.getProperty("many");
                if (count++ % 1000000 == 0) {
                    System.out.println("read 1000000 vertex, id = " + v.getId());
                }
            }
            stopWatch.stop();
            System.out.println("read " + NUMBER_TO_ITER + " = " + stopWatch.toString());
        } finally {
            g.shutdown();
        }
    }

}
