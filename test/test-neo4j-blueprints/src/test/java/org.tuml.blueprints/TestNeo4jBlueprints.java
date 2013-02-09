package org.tuml.blueprints;

import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jVertex;
import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.event.TransactionData;
import org.neo4j.graphdb.event.TransactionEventHandler;
import org.neo4j.kernel.lifecycle.LifecycleException;
import org.neo4j.tooling.GlobalGraphOperations;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.*;

/**
 * Date: 2013/01/26
 * Time: 2:53 PM
 */
public class TestNeo4jBlueprints {

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
    }

}
