package org.umlg.bitsy.test;

import com.lambdazen.bitsy.BitsyGraph;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

/**
 * Date: 2013/10/15
 * Time: 9:35 PM
 */
public class TestBitsy {

    @Test
    public void testSpeedDude1() throws IOException {
        File f = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "bitsy-performance");
        if (f.exists()) {
            FileUtils.deleteDirectory(f);
        }
        f.mkdir();
        Path dbPath = Paths.get(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "bitsy-performance");
        BitsyGraph g = new BitsyGraph(dbPath);
        try {

            int NUMBER_TO_ITER = 10;

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            Vertex one = g.addVertex(null);
            one.setProperty("one", "1");
            long previousSplitTime = 0;
            for (int i = 0; i < NUMBER_TO_ITER; i++) {
                Vertex many = g.addVertex(null);
                many.setProperty("many", "2");
                g.addEdge(null, one, many, "toMany");

                if (i != 0 && i % 1000000 == 0) {
                    stopWatch.split();
                    long splitTime = stopWatch.getSplitTime();
                    System.out.println(i + " " + stopWatch.toString() + " 100000 in " + (splitTime - previousSplitTime));
                    previousSplitTime = stopWatch.getSplitTime();
                    g.commit();
                }
            }
            g.commit();
            stopWatch.stop();
            System.out.println("write 10000000 = " + stopWatch.toString());

            stopWatch.reset();
            stopWatch.start();
            Vertex startV = g.getVertex(one.getId());
            int count = 1;
            for (Vertex v : startV.getVertices(Direction.OUT, "toMany")) {
                v.getProperty("many");
                if (count++ % 1000000 == 0) {
                    System.out.println("in vertex id = " + v.getId());
                }
            }
            stopWatch.stop();
            System.out.println("read " + NUMBER_TO_ITER + " = " + stopWatch.toString());
        } finally {
            long shutdownStart = System.currentTimeMillis();
            g.shutdown();
            System.out.println("Shutdown took " + (System.currentTimeMillis() - shutdownStart));
        }
    }

//    @Test
    public void testSpeedDude2() throws IOException {
        File f = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "bitsy-performance");
        if (f.exists()) {
            FileUtils.deleteDirectory(f);
        }
        f.mkdir();
        Path dbPath = Paths.get(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "bitsy-performance");
        BitsyGraph g = new BitsyGraph(dbPath);
        try {
            int NUMBER_TO_ITER = 10000000;

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            Object[] ids = new Object[(NUMBER_TO_ITER / 10) + 1];
            int objectIdCount = 0;

            Vertex previous = g.addVertex(null);
            previous.setProperty("one", "1");
            ids[objectIdCount++] = previous.getId();
            long previousSplitTime = 0;
            for (int i = 1; i <= NUMBER_TO_ITER; i++) {
                Vertex many = g.addVertex(null);
                many.setProperty("many", i);
                g.addEdge(null, previous, many, "toMany");

                if (i != 0 && i % 10 == 0) {
                    previous = many;
                    ids[objectIdCount++] = previous.getId();
                }

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
            System.out.println("write 10000000 = " + stopWatch.toString());

            stopWatch.reset();
            stopWatch.start();
            previousSplitTime = 0;
            int count = 0;
            for (Object id : ids) {
                count++;
                Vertex vertex = g.getVertex(id);

                for (Vertex v : vertex.getVertices(Direction.OUT, "toMany")) {
                    v.getProperty("many");
                }
                if (count == ids.length) {
                    Assert.assertEquals(0, count(vertex.getVertices(Direction.OUT, "toMany")));
                } else {
                    Assert.assertEquals(10, count(vertex.getVertices(Direction.OUT, "toMany")));
                }

                for (Vertex v : vertex.getVertices(Direction.OUT)) {
                    v.getProperty("many");
                }
                if (count == ids.length) {
                    Assert.assertEquals(0, count(vertex.getVertices(Direction.OUT)));
                } else {
                    Assert.assertEquals(10, count(vertex.getVertices(Direction.OUT)));
                }

                Long properId = (Long)id;
                if (properId != 0 && properId % 100000 == 0) {
                    stopWatch.split();
                    long splitTime = stopWatch.getSplitTime();
                    System.out.println(id + " " + stopWatch.toString() + " 100000 in " + (splitTime - previousSplitTime));
                    previousSplitTime = stopWatch.getSplitTime();
                }
            }
            stopWatch.stop();
            System.out.println("read 10000000 = " + stopWatch.toString());
        } finally {
            g.shutdown();
        }
    }

    protected int countIter(Iterator iter) {
        int count = 0;
        while (iter.hasNext()) {
            count++;
            iter.next();
        }
        return count;
    }

    public static int count(final Iterable iterable) {
        return count(iterable.iterator());
    }

    public static int count(final Iterator iterator) {
        int counter = 0;
        while (iterator.hasNext()) {
            iterator.next();
            counter++;
        }
        return counter;
    }

}
