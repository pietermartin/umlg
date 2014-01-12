package org.umlg.blueprints;

import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;
import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.event.TransactionData;
import org.neo4j.graphdb.event.TransactionEventHandler;
import org.neo4j.tooling.GlobalGraphOperations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.concurrent.*;

/**
 * Date: 2013/01/26
 * Time: 2:53 PM
 */
public class TestNeo4jBlueprints {

//    @Test
    public void testNeo4jInsertVertexPerformance() {
        File f = new File("/tmp/neo4j-performance");
        if (f.exists()) {
            f.delete();
        }
        f.mkdir();
        Neo4jGraph g = new Neo4jGraph(f.getAbsolutePath());
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long previousSplitTime = 0;
        for (int i = 0; i < 100000000; i++) {
            Vertex many = g.addVertex(null);
            many.setProperty("name", "123456");
            many.setProperty("surname", "123456");

            if (i != 0 && i % 1000000 == 0) {
                stopWatch.split();
                long splitTime = stopWatch.getSplitTime();
                System.out.println(i + " " + stopWatch.toString() + " 1000000 in " + (splitTime - previousSplitTime));
                previousSplitTime = stopWatch.getSplitTime();
                g.commit();
            }
        }
        g.commit();
        stopWatch.stop();
        System.out.println(stopWatch.toString());
    }


//    @Test
    public void testNeo4jPerformance() {
        File f = new File("/tmp/neo4j-performance");
        if (f.exists()) {
            f.delete();
        }
        f.mkdir();
        Neo4jGraph g = new Neo4jGraph(f.getAbsolutePath());
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Vertex one = g.addVertex(null);
        Long oneId = (Long)one.getId();
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
        System.out.println(stopWatch.toString());

        stopWatch.reset();
        stopWatch.start();
        Vertex startV = g.getVertex(oneId);
        for (Vertex v : startV.getVertices(Direction.OUT, "toMany")) {
            v.getProperty("many");
        }
        stopWatch.stop();
        System.out.println(stopWatch.toString());
    }

    @Test
    public void testIndexCreatedInAThreadUsedInAnother() throws IOException, InterruptedException, ExecutionException {
        final String url = "/tmp/blueprintstest2";
        File dir = new File(url);
        FileUtils.deleteDirectory(dir);
        final File f = new File(url);

        final IndexableGraph graph = new Neo4jGraph(f.getAbsolutePath());

        final Semaphore semaphore = new Semaphore(1);
        semaphore.acquire();

        ExecutorService es1 = Executors.newFixedThreadPool(1);
        Future<Boolean> future1 = es1.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                semaphore.acquire();
                Index<Vertex> index = graph.getIndex("testIndex1", Vertex.class);
                CloseableIterable<Vertex> iter = index.get("value", "value1");
                Assert.assertTrue(iter.iterator().hasNext());
                return true;
            }
        });

        ExecutorService es2 = Executors.newFixedThreadPool(1);
        Future<Boolean> future2 = es2.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                Index<Vertex> index = graph.createIndex("testIndex1", Vertex.class);
                Vertex v = graph.addVertex(null);
                index.put("value", "value1", v);
                ((TransactionalGraph) graph).commit();
                semaphore.release();
                return true;
            }
        });

        Assert.assertTrue(future1.get());
        graph.shutdown();
    }

    @Test
    public void testRollbackExceptionOnBeforeTxCommit() throws IOException {
        final String url = "/tmp/blueprintstest2";
        File dir = new File(url);
        FileUtils.deleteDirectory(dir);
        final File f = new File(url);
        Neo4jGraph graph = new Neo4jGraph(f.getAbsolutePath());
        GraphDatabaseService rawGraph = graph.getRawGraph();
        rawGraph.registerTransactionEventHandler(new TransactionEventHandler<Object>() {
            @Override
            public Object beforeCommit(TransactionData data) throws Exception {
                if (true) {
                    throw new RuntimeException("jippo validation exception");
                }
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void afterCommit(TransactionData data, Object state) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void afterRollback(TransactionData data, Object state) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        try {
            Vertex vertex = graph.addVertex(null);
            graph.commit();
        } catch (Exception e) {
            graph.rollback();
        }
        Assert.assertTrue(!GlobalGraphOperations.at(rawGraph).getAllNodes().iterator().hasNext());
        graph.shutdown();
    }

    @Test
    public void testNewInstanceInNewThreadFails() throws IOException, InterruptedException {
        final String url = "/tmp/blueprintstest2";
        File dir = new File(url);
        FileUtils.deleteDirectory(dir);
        final File f = new File(url);

        Neo4jGraph graph = new Neo4jGraph(f.getAbsolutePath());
        Vertex v1 = graph.addVertex(null);

        final Semaphore semaphore = new Semaphore(1);
        semaphore.acquire();

        ExecutorService es1 = Executors.newFixedThreadPool(1);
        Future<Boolean> future1 = es1.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    Neo4jGraph graph = new Neo4jGraph(f.getAbsolutePath());
                    Vertex v2 = graph.addVertex(null);
                    graph.commit();
                } finally {
                    semaphore.release();
                }
                return true;
            }
        });

        semaphore.acquire();
        graph.commit();

        Iterator<Node> iterator = GlobalGraphOperations.at(graph.getRawGraph()).getAllNodes().iterator();
        Assert.assertTrue(iterator.hasNext());
        iterator.next();
        Assert.assertFalse(iterator.hasNext());
        graph.shutdown();
    }

    @Test
    public void testMultipleEdgesBetweenNodes() throws IOException {
        final String url = "/tmp/blueprintstest2";
        File dir = new File(url);
        FileUtils.deleteDirectory(dir);
        final File f = new File(url);

        Neo4jGraph graph = new Neo4jGraph(f.getAbsolutePath());
        Vertex v1 = graph.addVertex(null);
        Vertex v2 = graph.addVertex(null);
        Edge edge1 = graph.addEdge(null, v1, v1, "test");
        Edge edge2 = graph.addEdge(null, v1, v1, "test");
        graph.commit();
        Assert.assertNotSame(edge1, edge2);
        v1 = graph.getVertex(v1.getId());
        Iterator<Edge> iterator = v1.getEdges(Direction.OUT, "test").iterator();
        Assert.assertEquals(edge1, iterator.next());
        Assert.assertEquals(edge2, iterator.next());
        graph.shutdown();
    }

//    @Test
    public void testSpeed() throws IOException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final String url = "/tmp/blueprintstest2";
        File dir = new File(url);
        FileUtils.deleteDirectory(dir);
        final File f = new File(url);
        Neo4jGraph graph = new Neo4jGraph(f.getAbsolutePath());
        Vertex v1 = graph.addVertex(null);
        v1.setProperty("this", "that");
        for (int i = 0; i < 1000000; i++) {
            Vertex v2 = graph.addVertex(null);
            v2.setProperty("this", "that");
            graph.addEdge(null, v1, v2, "test");
            if (i % 10000 == 0) {
                graph.commit();
                System.out.println(i + " " + stopWatch.toString());
            }
        }
        graph.commit();
        stopWatch.stop();
        System.out.println(stopWatch.toString());
        graph.shutdown();
    }

    @Test
    public void testNeo4jPropertyDifferentType() {
        File f = new File("/tmp/neo4j-performance");
        if (f.exists()) {
            f.delete();
        }
        f.mkdir();
        Neo4jGraph g = new Neo4jGraph(f.getAbsolutePath());
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Vertex one = g.addVertex(null);
        one.setProperty("one", 1);

        Vertex two = g.addVertex(null);
        two.setProperty("one", "2");

        g.commit();
        stopWatch.stop();
        System.out.println(stopWatch.toString());

    }


//    @Test
    public void testSpeedDude1() throws IOException {
        File f = new File("/tmp/neo4j-performance");
        if (f.exists()) {
            FileUtils.deleteDirectory(f);
        }
        f.mkdir();
        Neo4jGraph g = new Neo4jGraph(f.getAbsolutePath());
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
            g.shutdown();
        }
    }

//    @Test
    public void testSpeedDude2() throws IOException {
        File f = new File("/tmp/neo4j-performance");
        if (f.exists()) {
            FileUtils.deleteDirectory(f);
        }
        f.mkdir();
        Neo4jGraph g = new Neo4jGraph(f.getAbsolutePath());
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
        File f = new File("/tmp/neo4j-performance");
        if (f.exists()) {
            FileUtils.deleteDirectory(f);
        }
        f.mkdir();
        Neo4jGraph g = new Neo4jGraph(f.getAbsolutePath());
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
                Assert.assertEquals(sb.toString(), next.getProperty("many1"));
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

    @Test
    public void testRemoveTransactionAlreadyWritable() throws IOException {
        File f = new File("/tmp/neo4j-performance");
        if (f.exists()) {
            FileUtils.deleteDirectory(f);
        }
        f.mkdir();
        Neo4jGraph graph = new Neo4jGraph(f.getAbsolutePath());
        try {
            graph.createKeyIndex("name", Vertex.class);
            graph.commit();

            Vertex v1 = graph.addVertex(null);
            v1.setProperty("name", "aaaa");
            for (int i = 0; i < 10; i++) {
                Vertex v2 = graph.addVertex(null);
                v2.setProperty("name", "bbbb");
                Edge e = graph.addEdge(null, v1, v2, "label1");
                e.setProperty("name", "cccc");
            }
            graph.commit();

            org.junit.Assert.assertEquals(11, count(graph.getVertices()) - 1);
            org.junit.Assert.assertEquals(1, count(graph.getVertices("name", "aaaa")));
            org.junit.Assert.assertEquals(10, count(graph.getVertices("name", "bbbb")));
            org.junit.Assert.assertEquals(10, count(graph.getEdges("name", "cccc")));

            Iterator<Vertex> iter = graph.getVertices("name", "bbbb").iterator();
            Vertex v = iter.next();
            long removedId = (Long)v.getId();
//            v.setProperty("name", "bbbba");
//            org.junit.Assert.assertEquals("bbbba", v.getProperty("name"));
            iter.remove();
            org.junit.Assert.assertNull(graph.getVertex(removedId));
            org.junit.Assert.assertEquals(9, count(iter));

            graph.commit();
            iter = graph.getVertices("name", "bbbb").iterator();
            org.junit.Assert.assertEquals(9, count(iter));

        } finally {
            graph.shutdown();
        }
    }


//    @Test
    public void testIndexSpeed() throws IOException {
        File f = new File("/tmp/neo4j-performance");
        if (f.exists()) {
            FileUtils.deleteDirectory(f);
        }
        f.mkdir();
        Neo4jGraph g = new Neo4jGraph(f.getAbsolutePath());
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

            for (int i = 0; i < NUMBER_TO_ITER; i++) {
                Iterator<Vertex> iter = g.getVertices("many", i).iterator();
                Assert.assertTrue(iter.hasNext());

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
