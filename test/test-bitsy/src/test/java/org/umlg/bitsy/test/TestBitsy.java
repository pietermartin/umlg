package org.umlg.bitsy.test;

import com.lambdazen.bitsy.BitsyGraph;
import com.lambdazen.bitsy.wrapper.BitsyAutoReloadingGraph;
import com.tinkerpop.blueprints.Vertex;
import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Date: 2013/10/15
 * Time: 9:35 PM
 */
public class TestBitsy {

    @Test
    public void testBitsyPerformance() {
        File f = new File("/tmp/bitsy-performance");
        if (f.exists()) {
            f.delete();
        }
        f.mkdir();
        Path dbPath = Paths.get("/tmp/bitsy-performance");
        BitsyGraph g = new BitsyGraph(dbPath);
//        BitsyAutoReloadingGraph g = new BitsyAutoReloadingGraph(bitsyGraph);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Vertex one = g.addVertex(null);
        one.setProperty("one", "1");
        long previousSplitTime = 0;
        for (int i = 0; i < 100000; i++) {
            Vertex many = g.addVertex(null);
            many.setProperty("many", "2");
            g.addEdge(null, one, many, "toMany");

            if (i % 1000 == 0) {
                stopWatch.split();
                long splitTime = stopWatch.getSplitTime();
                System.out.println(i + " " + stopWatch.toString() + " 1000 in " + (splitTime - previousSplitTime));
                previousSplitTime = stopWatch.getSplitTime();
                g.commit();
            }
        }
        g.commit();
        stopWatch.stop();
        System.out.println(stopWatch.toString());
    }
}
