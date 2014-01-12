package org.umlg.titan.test;

import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Date: 2013/11/25
 * Time: 8:59 PM
 */
public class TestTitanSpeed {

    TitanGraph graph;
    private Throwable toThrow;
    Random rand = new Random();

//    @Test
    public void testTitanInsertVertexPerformance() {
        File f = new File("/tmp/titan-performance");
        if (f.exists()) {
            f.delete();
        }
        f.mkdir();
        Configuration propertiesConfiguration = new PropertiesConfiguration();
//        propertiesConfiguration.addProperty("storage.backend", "local");
        propertiesConfiguration.addProperty("storage.backend", "persistit");
        propertiesConfiguration.addProperty("storage.directory", f.getAbsolutePath());
        TitanGraph g = TitanFactory.open(propertiesConfiguration);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long previousSplitTime = 0;
        for (int i = 0; i < 100000000; i++) {
            Vertex many = g.addVertex(null);
            many.setProperty("name", "123456");
            many.setProperty("surname", "123456");

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
        System.out.println(stopWatch.toString());
    }


//    @Test
    public void testTitanPerformance() {
        File f = new File("/tmp/neo4j-performance");
        if (f.exists()) {
            f.delete();
        }
        f.mkdir();
        Configuration propertiesConfiguration = new PropertiesConfiguration();
//        propertiesConfiguration.addProperty("storage.backend", "local");
        propertiesConfiguration.addProperty("storage.backend", "persistit");
        propertiesConfiguration.addProperty("storage.directory", f.getAbsolutePath());
        TitanGraph g = TitanFactory.open(propertiesConfiguration);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Vertex one = g.addVertex(null);
        one.setProperty("one", "1");
        long previousSplitTime = 0;
        for (int i = 0; i < 100000000; i++) {
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
        System.out.println(stopWatch.toString());
    }

//    @Test
    public void testMultiThreadedReadsOnBipartiteGraph() throws Exception {

        File f = new File("/tmp/neo4j-performance");
        if (f.exists()) {
            f.delete();
        }
        f.mkdir();
        Configuration propertiesConfiguration = new PropertiesConfiguration();
//        propertiesConfiguration.addProperty("storage.backend", "local");
        propertiesConfiguration.addProperty("storage.backend", "persistit");
        propertiesConfiguration.addProperty("storage.directory", f.getAbsolutePath());
        graph = TitanFactory.open(propertiesConfiguration);


        final int numVertices = 1000000; // 100K vertices
        final int numIters = 100000;
        final int numElements = 8 * numIters; // expected to visit 8 v/e per iteration
        final int partSize = numVertices / 2;
        final int numPerCommit = 1000;

        final String label = "test";
        final Object[] outVertices = new Object[partSize];
        final Object[] inVertices = new Object[partSize];

        // Vertices
        for (int i=0; i < partSize; i++) {
            outVertices[i] = graph.addVertex(null).getId();
            inVertices[i] = graph.addVertex(null).getId();

            if (i % numPerCommit == 0) {
                graph.commit();
            }
        }

        // Edges
        for (int i=0; i < partSize; i++) {
            Vertex outVertex = graph.getVertex(outVertices[i]);
            outVertex.addEdge(label, graph.getVertex(inVertices[(5 * i + 1) % partSize]));
            outVertex.addEdge(label, graph.getVertex(inVertices[(5 * i + 4) % partSize]));
            outVertex.addEdge(label, graph.getVertex(inVertices[(5 * i + 7) % partSize]));

            if (i % numPerCommit == 0) {
                graph.commit();
            }
        }
        graph.commit();

        final int numRuns = 3;
        Map<Integer, String> calcStrMap = new HashMap<Integer, String>();

        for (int run=0; run < numRuns; run++) {
//            ((BitsyGraph)graph).setDefaultIsolationLevel(BitsyIsolationLevel.READ_COMMITTED);
            for (final int numThreads : new int[] {1, 2, 3, 4, 5, 10, 25, 50, 100, 150, 250, 500, 750, 1000}) {
                ExecutorService service = Executors.newFixedThreadPool(numThreads);

                final CountDownLatch cdl = new CountDownLatch(numThreads);
                long ts = System.currentTimeMillis();

//                ((BitsyGraph)graph).setDefaultIsolationLevel(BitsyIsolationLevel.READ_COMMITTED);
                System.out.println("Running bi-partite read test with " + numThreads + " threads");
                for (int i = 0; i < numThreads; i++) {
                    final int tid = i;
                    //System.out.println("Scheduling read work for thread " + tid);
                    service.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Vertex v = graph.getVertex(outVertices[0]);
                                long startTime = System.currentTimeMillis();

                                for (int k=0; k < 100 * numIters / numThreads; k++) {
                                    Assert.assertNotNull(v);

                                    Vertex nextV = randomVertex(v.getVertices(Direction.OUT, label));

                                    Assert.assertNotNull(nextV);

                                    // Take a random edge back
                                    Vertex backV = randomVertex(nextV.getVertices(Direction.IN));
                                    if (backV != null) {
                                        v = backV;
                                    }

                                    if (k % 1000000 == 0) {
                                        long endTime = System.currentTimeMillis();
                                        System.out.println(endTime - startTime);
                                        startTime = endTime;
                                    }

                                }

                                //System.out.println("Thread " + tid  + " is done");
                            } catch (Throwable t) {
                                setException(t);
                            } finally {
                                cdl.countDown();
                            }
                        }

                        private Vertex randomVertex(Iterable<Vertex> vertices) {
                            List<Vertex> options = new ArrayList<Vertex>();
                            for (Vertex option : vertices) {
                                options.add(option);
                            }

                            if (options.isEmpty()) {
                                return null;
                            } else {
                                return options.get(rand.nextInt(options.size()));
                            }
                        }
                    });
                }

                cdl.await();

                if (getException() != null) {
                    throw new RuntimeException("Error in testMultiThreadedReadsOnBipartiteGraph", getException());
                }

                service.shutdown();

                long duration = System.currentTimeMillis() - ts;
                double tps = ((double)numElements * 100000 / duration);
                System.out.println("Took " + duration + "ms to query " + numElements + " vertices+edge 100 times. Rate = " + (duration / numElements) + "ms per vertex. TPS = " + tps);

                String calcStr = calcStrMap.get(numThreads);
                if (calcStr == null) {
                    calcStrMap.put(numThreads, "=(" + tps);
                } else {
                    calcStrMap.put(numThreads, calcStr + " + " + tps);
                }
            }
        }

        for (Map.Entry<Integer, String> entry : calcStrMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + ")/3");
        }

//        ((BitsyGraph)graph).setDefaultIsolationLevel(BitsyIsolationLevel.REPEATABLE_READ);
    }

//    @Test
    public void testSpeedDude() {
        File f = new File("/tmp/neo4j-performance");
        if (f.exists()) {
            f.delete();
        }
        f.mkdir();
        Configuration propertiesConfiguration = new PropertiesConfiguration();
//        propertiesConfiguration.addProperty("storage.backend", "local");
        propertiesConfiguration.addProperty("storage.backend", "persistit");
        propertiesConfiguration.addProperty("storage.directory", f.getAbsolutePath());
        TitanGraph g = TitanFactory.open(propertiesConfiguration);
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            Vertex one = g.addVertex(null);
            one.setProperty("one", "1");
            long previousSplitTime = 0;
            for (int i = 0; i < 10000000; i++) {
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
            System.out.println("write 10000000 = " + stopWatch.toString());

            stopWatch.reset();
            stopWatch.start();
            Vertex startV = g.getVertex(one.getId());
            for (Vertex v : startV.getVertices(Direction.OUT, "toMany")) {
                v.getProperty("many");
            }
            stopWatch.stop();
            System.out.println("read 10000000 = " + stopWatch.toString());
        } finally {
            g.shutdown();
        }
    }

//    @Test
    public void testSpeedDude2() throws IOException {
        final String url = "/tmp/titan-test";
        File dir = new File(url);
        FileUtils.deleteDirectory(dir);
        final File f = new File(url);
        Configuration propertiesConfiguration = new PropertiesConfiguration();
        propertiesConfiguration.addProperty("storage.backend", "persistit");
        propertiesConfiguration.addProperty("storage.directory", f.getAbsolutePath());
        TitanGraph g = TitanFactory.open(propertiesConfiguration);
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
            System.out.println("write " + NUMBER_TO_ITER + " = " + stopWatch.toString());

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
                    junit.framework.Assert.assertEquals(0, count(vertex.getVertices(Direction.OUT, "toMany")));
                } else {
                    junit.framework.Assert.assertEquals(10, count(vertex.getVertices(Direction.OUT, "toMany")));
                }

                for (Vertex v : vertex.getVertices(Direction.OUT)) {
                    v.getProperty("many");
                }
                if (count == ids.length) {
                    junit.framework.Assert.assertEquals(0, count(vertex.getVertices(Direction.OUT)));
                } else {
                    junit.framework.Assert.assertEquals(10, count(vertex.getVertices(Direction.OUT)));
                }

                if (count % 100000 == 0) {
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

//    @Test
    public void testSpeedLinkedList() throws IOException {
        final String url = "/tmp/titan-test";
        File dir = new File(url);
        FileUtils.deleteDirectory(dir);
        final File f = new File(url);
        Configuration propertiesConfiguration = new PropertiesConfiguration();
        propertiesConfiguration.addProperty("storage.backend", "local");
        propertiesConfiguration.addProperty("storage.directory", f.getAbsolutePath());
        TitanGraph g = TitanFactory.open(propertiesConfiguration);
        try {

            int NUMBER_TO_ITER = 10000000;

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            Vertex one = g.addVertex(null);
            Vertex start = one;
            one.setProperty("one", "1");
            StringBuilder sb = new StringBuilder();
            sb.append("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            sb.append("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
            sb.append("cccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc");
            sb.append("dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
            sb.append("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
            long previousSplitTime = 0;
            for (int i = 0; i < NUMBER_TO_ITER; i++) {
                Vertex many = g.addVertex(null);
                many.setProperty("many1", sb.toString());
                many.setProperty("many2", sb.toString());
                many.setProperty("many3", sb.toString());
                many.setProperty("many4", sb.toString());
                many.setProperty("many5", sb.toString());
                g.addEdge(null, one, many, "toMany");
                one = many;

                if (i != 0 && i % 10000 == 0) {
                    g.commit();
                }
                if (i != 0 && i % 1000000 == 0) {
                    stopWatch.split();
                    long splitTime = stopWatch.getSplitTime();
                    System.out.println(i + " " + stopWatch.toString() + " 100000 in " + (splitTime - previousSplitTime));
                    previousSplitTime = stopWatch.getSplitTime();
                }
            }
            g.commit();
            stopWatch.stop();
            System.out.println("write 10000000 = " + stopWatch.toString());

            stopWatch.reset();
            stopWatch.start();
            int count = 1;
            Vertex startV = g.getVertex(start.getId());
            Iterator<Vertex> vertices = startV.getVertices(Direction.OUT).iterator();
            while (vertices.hasNext()) {
                Vertex next = vertices.next();
                junit.framework.Assert.assertEquals(sb.toString(), next.getProperty("many1"));
                vertices = next.getVertices(Direction.OUT).iterator();
                if (count++ % 1000000 == 0) {
                    System.out.println("next vertex id = " + next.getId());
                }
            }

            stopWatch.stop();
            System.out.println("read " + NUMBER_TO_ITER + " = " + stopWatch.toString());
        } finally {
            g.shutdown();
        }
    }


    private void setException(Throwable t) {
        this.toThrow = t;
    }

    private Throwable getException() {
        return toThrow;
    }

    protected int countIter(Iterator iter) {
        int count = 0;
        while (iter.hasNext()) {
            count++;
            iter.next();
        }
        return count;
    }

//    @Test
    public void testIndexSpeed() throws IOException {
        final String url = "/tmp/titan-test";
        File dir = new File(url);
        FileUtils.deleteDirectory(dir);
        final File f = new File(url);
        Configuration propertiesConfiguration = new PropertiesConfiguration();
        propertiesConfiguration.addProperty("storage.backend", "persistit");
        propertiesConfiguration.addProperty("storage.directory", f.getAbsolutePath());
        TitanGraph g = TitanFactory.open(propertiesConfiguration);
        try {

            g.createKeyIndex("many", Vertex.class);

            int NUMBER_TO_ITER = 10000000;

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            Vertex one = g.addVertex(null);
            one.setProperty("one", -1);
            long previousSplitTime = 0;
            for (int i = 0; i < NUMBER_TO_ITER; i++) {
                Vertex many = g.addVertex(null);
                many.setProperty("many", i);
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
            System.out.println("write " + NUMBER_TO_ITER + " = " + stopWatch.toString());

            stopWatch.reset();
            stopWatch.start();

            for (int i = 0; i < NUMBER_TO_ITER; i++) {
                Iterator<Vertex> iter = g.getVertices("many", i).iterator();
                junit.framework.Assert.assertTrue(iter.hasNext());

                if (i != 0 && i % 1000000 == 0) {
                    stopWatch.split();
                    long splitTime = stopWatch.getSplitTime();
                    System.out.println(i + " " + stopWatch.toString() + " 1000000 in " + (splitTime - previousSplitTime));
                    previousSplitTime = stopWatch.getSplitTime();
                }

            }

            stopWatch.stop();
            System.out.println("read " + NUMBER_TO_ITER + " = " + stopWatch.toString());
        } finally {
            g.shutdown();
        }
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
