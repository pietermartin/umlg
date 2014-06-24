package org.umlg.blueprints;

import com.tinkerpop.gremlin.neo4j.structure.Neo4jGraph;
import com.tinkerpop.gremlin.structure.Edge;
import com.tinkerpop.gremlin.structure.Vertex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.event.TransactionData;
import org.neo4j.graphdb.event.TransactionEventHandler;
import org.neo4j.tooling.GlobalGraphOperations;

import java.io.File;
import java.util.Iterator;
import java.util.concurrent.*;

/**
 * Date: 2013/01/26
 * Time: 2:53 PM
 */
public class TestNeo4jBlueprints {

//    @Test
    public void testNeo4jInsertVertexPerformance() {

        File f = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "neo4j-performance");
        if (f.exists()) {
            f.delete();
        }
        f.mkdir();
        Neo4jGraph g = Neo4jGraph.open(f.getAbsolutePath());
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long previousSplitTime = 0;
        for (int i = 0; i < 100000000; i++) {
            Vertex many = g.addVertex();
            many.property("name", "123456");
            many.property("surname", "123456");

            if (i != 0 && i % 1000000 == 0) {
                stopWatch.split();
                long splitTime = stopWatch.getSplitTime();
                System.out.println(i + " " + stopWatch.toString() + " 1000000 in " + (splitTime - previousSplitTime));
                previousSplitTime = stopWatch.getSplitTime();
                g.tx().commit();
            }
        }
        g.tx().commit();
        stopWatch.stop();
        System.out.println(stopWatch.toString());
    }


//    @Test
    public void testNeo4jPerformance() {
        File f = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "neo4j-performance");
        if (f.exists()) {
            f.delete();
        }
        f.mkdir();
        Neo4jGraph g = Neo4jGraph.open(f.getAbsolutePath());
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Vertex one = g.addVertex();
        Long oneId = (Long)one.id();
        one.property("one", "1");
        long previousSplitTime = 0;
        for (int i = 0; i < 10000000; i++) {
            Vertex many = g.addVertex();
            many.property("many", "2");
            one.addEdge("toMany", many);

            if (i != 0 && i % 100000 == 0) {
                stopWatch.split();
                long splitTime = stopWatch.getSplitTime();
                System.out.println(i + " " + stopWatch.toString() + " 100000 in " + (splitTime - previousSplitTime));
                previousSplitTime = stopWatch.getSplitTime();
                g.tx().commit();
            }
        }
        g.tx().commit();
        stopWatch.stop();
        System.out.println(stopWatch.toString());

        stopWatch.reset();
        stopWatch.start();
        Vertex startV = g.v(oneId);
        for (Vertex v : startV.out("toMany").toList()) {
            v.value("many");
        }
        stopWatch.stop();
        System.out.println(stopWatch.toString());
    }

    @Test
    public void testRollbackExceptionOnBeforeTxCommit() throws Exception {
        final String url = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "blueprintstest2";
        File dir = new File(url);
        FileUtils.deleteDirectory(dir);
        final File f = new File(url);
        Neo4jGraph graph = Neo4jGraph.open(f.getAbsolutePath());
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
            Vertex vertex = graph.addVertex();
            graph.tx().commit();
        } catch (Exception e) {
            graph.tx().rollback();
        }
        graph.addVertex();
        int count = countIter(GlobalGraphOperations.at(rawGraph).getAllNodes().iterator());
        Assert.assertEquals(1, count);
        graph.close();
    }

    @Test
    public void testNewInstanceInNewThreadFails() throws Exception {
        final String url = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "blueprintstest2";
        File dir = new File(url);
        FileUtils.deleteDirectory(dir);
        final File f = new File(url);

        Neo4jGraph graph = Neo4jGraph.open(f.getAbsolutePath());
        Vertex v1 = graph.addVertex();

        final Semaphore semaphore = new Semaphore(1);
        semaphore.acquire();

        ExecutorService es1 = Executors.newFixedThreadPool(1);
        Future<Boolean> future1 = es1.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    Neo4jGraph graph = Neo4jGraph.open(f.getAbsolutePath());
                    Vertex v2 = graph.addVertex();
                    graph.tx().commit();
                } finally {
                    semaphore.release();
                }
                return true;
            }
        });

        semaphore.acquire();
        graph.tx().commit();

        graph.addVertex();
        Iterator<Node> iterator = GlobalGraphOperations.at(graph.getRawGraph()).getAllNodes().iterator();
        Assert.assertTrue(iterator.hasNext());
        iterator.next();
        Assert.assertTrue(iterator.hasNext());
        iterator.next();
        Assert.assertFalse(iterator.hasNext());
        graph.close();
    }

    @Test
    public void testMultipleEdgesBetweenNodes() throws Exception {
        final String url = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "blueprintstest2";
        File dir = new File(url);
        FileUtils.deleteDirectory(dir);
        final File f = new File(url);

        Neo4jGraph graph = Neo4jGraph.open(f.getAbsolutePath());
        Vertex v1 = graph.addVertex();
        Vertex v2 = graph.addVertex();
        Edge edge1 = v1.addEdge("test", v1);
        Edge edge2 = v1.addEdge("test", v1);
        graph.tx().commit();
        Assert.assertNotSame(edge1, edge2);
        v1 = graph.v(v1.id());
        Iterator<Edge> iterator = v1.outE("test");
        Assert.assertEquals(edge1, iterator.next());
        Assert.assertEquals(edge2, iterator.next());
        graph.close();
    }

    @Test
    public void testSpeed() throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final String url = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "blueprintstest2";
        File dir = new File(url);
        FileUtils.deleteDirectory(dir);
        final File f = new File(url);
        Neo4jGraph graph = Neo4jGraph.open(f.getAbsolutePath());
        Vertex v1 = graph.addVertex();
        v1.property("this", "that");
        for (int i = 0; i < 1000000; i++) {
            Vertex v2 = graph.addVertex();
            v2.property("this", "that");
            v1.addEdge("test", v2);
            if (i % 10000 == 0) {
                graph.tx().commit();
                System.out.println(i + " " + stopWatch.toString());
            }
        }
        graph.tx().commit();
        stopWatch.stop();
        System.out.println(stopWatch.toString());
        graph.close();
    }

    @Test
    public void testNeo4jPropertyDifferentType() {
        File f = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "neo4j-performance");
        if (f.exists()) {
            f.delete();
        }
        f.mkdir();
        Neo4jGraph g = Neo4jGraph.open(f.getAbsolutePath());
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Vertex one = g.addVertex();
        one.property("one", 1);

        Vertex two = g.addVertex();
        two.property("one", "2");

        g.tx().commit();
        stopWatch.stop();
        System.out.println(stopWatch.toString());
    }


//    @Test
    public void testSpeedDude1() throws Exception {
        File f = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "neo4j-performance");
        if (f.exists()) {
            FileUtils.deleteDirectory(f);
        }
        f.mkdir();
        Neo4jGraph g = Neo4jGraph.open(f.getAbsolutePath());
        try {

            int NUMBER_TO_ITER = 10000000;

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            Vertex one = g.addVertex();
            one.property("one", "1");
            long previousSplitTime = 0;
            for (int i = 0; i < NUMBER_TO_ITER; i++) {
                Vertex many = g.addVertex();
                many.property("many", "2");
                one.addEdge("toMany", many);

                if (i != 0 && i % 1000000 == 0) {
                    stopWatch.split();
                    long splitTime = stopWatch.getSplitTime();
                    System.out.println(i + " " + stopWatch.toString() + " 100000 in " + (splitTime - previousSplitTime));
                    previousSplitTime = stopWatch.getSplitTime();
                    g.tx().commit();
                }
            }
            g.tx().commit();
            stopWatch.stop();
            System.out.println("write 10000000 = " + stopWatch.toString());

            stopWatch.reset();
            stopWatch.start();
            Vertex startV = g.v(one.id());
            int count = 1;
            for (Vertex v : startV.out("toMany").toList()) {
                v.value("many");
                if (count++ % 1000000 == 0) {
                    System.out.println("in vertex id = " + v.id());
                }
            }
            stopWatch.stop();
            System.out.println("read " + NUMBER_TO_ITER + " = " + stopWatch.toString());
        } finally {
            g.close();
        }
    }

//    @Test
    public void testSpeedDude2() throws Exception {
        File f = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "neo4j-performance");
        if (f.exists()) {
            FileUtils.deleteDirectory(f);
        }
        f.mkdir();
        Neo4jGraph g = Neo4jGraph.open(f.getAbsolutePath());
        try {
            int NUMBER_TO_ITER = 10000000;

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            Object[] ids = new Object[(NUMBER_TO_ITER / 10) + 1];
            int objectIdCount = 0;

            Vertex previous = g.addVertex();
            previous.property("one", "1");
            ids[objectIdCount++] = previous.id();
            long previousSplitTime = 0;
            for (int i = 1; i <= NUMBER_TO_ITER; i++) {
                Vertex many = g.addVertex();
                many.property("many", i);
                previous.addEdge("toMany", many);

                if (i != 0 && i % 10 == 0) {
                    previous = many;
                    ids[objectIdCount++] = previous.id();
                }

                if (i != 0 && i % 100000 == 0) {
                    stopWatch.split();
                    long splitTime = stopWatch.getSplitTime();
                    System.out.println(i + " " + stopWatch.toString() + " 100000 in " + (splitTime - previousSplitTime));
                    previousSplitTime = stopWatch.getSplitTime();
                    g.tx().commit();
                }
            }
            g.tx().commit();
            stopWatch.stop();
            System.out.println("write " + NUMBER_TO_ITER + " = " + stopWatch.toString());

            stopWatch.reset();
            stopWatch.start();
            previousSplitTime = 0;
            int count = 0;
            for (Object id : ids) {
                count++;
                Vertex vertex = g.v(id);

                for (Vertex v : vertex.out("toMany").toList()) {
                    v.value("many");
                }
                if (count == ids.length) {
                    Assert.assertEquals(0, count(vertex.out("toMany").toList()));
                } else {
                    Assert.assertEquals(10, count(vertex.out("toMany").toList()));
                }

                for (Vertex v : vertex.out().toList()) {
                    v.value("many");
                }
                if (count == ids.length) {
                    Assert.assertEquals(0, count(vertex.out()));
                } else {
                    Assert.assertEquals(10, count(vertex.out()));
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
            g.close();
        }
    }

//    @Test
    public void testSpeedLinkedList() throws Exception {
        File f = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "neo4j-performance");
        if (f.exists()) {
            FileUtils.deleteDirectory(f);
        }
        f.mkdir();
        Neo4jGraph g = Neo4jGraph.open(f.getAbsolutePath());
        try {

            int NUMBER_TO_ITER = 10000000;

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            Vertex one = g.addVertex();
            Vertex start = one;
            one.property("one", "1");
            StringBuilder sb = new StringBuilder();
            sb.append("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            sb.append("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
            sb.append("cccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc");
            sb.append("dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
            sb.append("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
            long previousSplitTime = 0;
            for (int i = 0; i < NUMBER_TO_ITER; i++) {
                Vertex many = g.addVertex();
                many.property("many1", sb.toString());
                many.property("many2", sb.toString());
                many.property("many3", sb.toString());
                many.property("many4", sb.toString());
                many.property("many5", sb.toString());
                one.addEdge("toMany", many);
                one = many;

                if (i != 0 && i % 10000 == 0) {
                    g.tx().commit();
                }
                if (i != 0 && i % 1000000 == 0) {
                    stopWatch.split();
                    long splitTime = stopWatch.getSplitTime();
                    System.out.println(i + " " + stopWatch.toString() + " 100000 in " + (splitTime - previousSplitTime));
                    previousSplitTime = stopWatch.getSplitTime();
                }
            }
            g.tx().commit();
            stopWatch.stop();
            System.out.println("write 10000000 = " + stopWatch.toString());

            stopWatch.reset();
            stopWatch.start();
            int count = 1;
            Vertex startV = g.v(start.id());
            Iterator<Vertex> vertices = startV.out();
            while (vertices.hasNext()) {
                Vertex next = vertices.next();
                Assert.assertEquals(sb.toString(), next.value("many1"));
                vertices = next.out();
                if (count++ % 1000000 == 0) {
                    System.out.println("next vertex id = " + next.id());
                }
            }

            stopWatch.stop();
            System.out.println("read " + NUMBER_TO_ITER + " = " + stopWatch.toString());
        } finally {
            g.close();
        }
    }

////    @Test
//    public void testRemoveTransactionAlreadyWritable() throws IOException {
//        File f = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "neo4j-performance");
//        if (f.exists()) {
//            FileUtils.deleteDirectory(f);
//        }
//        f.mkdir();
//        Neo4jGraph graph = Neo4jGraph.open(f.getAbsolutePath());
//        try {
//            graph.createKeyIndex("name", Vertex.class);
//            graph.commit();
//
//            Vertex v1 = graph.addVertex();
//            v1.property("name", "aaaa");
//            for (int i = 0; i < 10; i++) {
//                Vertex v2 = graph.addVertex();
//                v2.property("name", "bbbb");
//                Edge e = graph.addEdge(null, v1, v2, "label1");
//                e.property("name", "cccc");
//            }
//            graph.commit();
//
//            org.junit.Assert.assertEquals(11, count(graph.getVertices()) - 1);
//            org.junit.Assert.assertEquals(1, count(graph.getVertices("name", "aaaa")));
//            org.junit.Assert.assertEquals(10, count(graph.getVertices("name", "bbbb")));
//            org.junit.Assert.assertEquals(10, count(graph.getEdges("name", "cccc")));
//
//            Iterator<Vertex> iter = graph.getVertices("name", "bbbb").iterator();
//            Vertex v = iter.next();
//            long removedId = (Long)v.getId();
////            v.property("name", "bbbba");
////            org.junit.Assert.assertEquals("bbbba", v.getProperty("name"));
//            iter.remove();
//            org.junit.Assert.assertNull(graph.getVertex(removedId));
//            org.junit.Assert.assertEquals(9, count(iter));
//
//            graph.commit();
//            iter = graph.getVertices("name", "bbbb").iterator();
//            org.junit.Assert.assertEquals(9, count(iter));
//
//        } finally {
//            graph.shutdown();
//        }
//    }


////    @Test
//    public void testIndexSpeed() throws IOException {
//        File f = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "neo4j-performance");
//        if (f.exists()) {
//            FileUtils.deleteDirectory(f);
//        }
//        f.mkdir();
//        Neo4j2Graph g = new Neo4j2Graph(f.getAbsolutePath());
//        try {
//
//            g.createKeyIndex("many", Vertex.class);
//
//            int NUMBER_TO_ITER = 10000000;
//
//            StopWatch stopWatch = new StopWatch();
//            stopWatch.start();
//
//            Vertex one = g.addVertex();
//            one.property("one", -1);
//            long previousSplitTime = 0;
//            for (int i = 0; i < NUMBER_TO_ITER; i++) {
//                Vertex many = g.addVertex();
//                many.property("many", i);
//                g.addEdge(null, one, many, "toMany");
//
//                if (i != 0 && i % 100000 == 0) {
//                    stopWatch.split();
//                    long splitTime = stopWatch.getSplitTime();
//                    System.out.println(i + " " + stopWatch.toString() + " 100000 in " + (splitTime - previousSplitTime));
//                    previousSplitTime = stopWatch.getSplitTime();
//                    g.commit();
//                }
//            }
//            g.commit();
//            stopWatch.stop();
//            System.out.println("write 10000000 = " + stopWatch.toString());
//
//            stopWatch.reset();
//            stopWatch.start();
//
//            for (int i = 0; i < NUMBER_TO_ITER; i++) {
//                Iterator<Vertex> iter = g.getVertices("many", i).iterator();
//                Assert.assertTrue(iter.hasNext());
//
//                if (i != 0 && i % 1000000 == 0) {
//                    stopWatch.split();
//                    long splitTime = stopWatch.getSplitTime();
//                    System.out.println(i + " " + stopWatch.toString() + " 1000000 in " + (splitTime - previousSplitTime));
//                    previousSplitTime = stopWatch.getSplitTime();
//                }
//
//            }
//
//            stopWatch.stop();
//            System.out.println("read " + NUMBER_TO_ITER + " = " + stopWatch.toString());
//        } finally {
//            g.shutdown();
//        }
//    }


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
