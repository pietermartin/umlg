package org.tuml.blueprints;

import com.orientechnologies.orient.core.db.graph.OGraphDatabase;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.index.OIndex;
import com.orientechnologies.orient.core.index.OIndexManagerProxy;
import com.orientechnologies.orient.core.index.OIndexTxAwareMultiValue;
import com.orientechnologies.orient.core.index.OSimpleKeyIndexDefinition;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.tx.OTransaction;
import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.orient.*;
import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.*;

/**
 * Date: 2013/01/26
 * Time: 2:53 PM
 */
public class TestOrientDbBlueprints {

    @Test
    public void testIndexCreatedInAThreadUsedInAnother() throws IOException, InterruptedException, ExecutionException {
        final String url = "/tmp/blueprintstest2";
        File dir = new File(url);
        FileUtils.deleteDirectory(dir);
        final File f = new File(url);

        final IndexableGraph graph = new OrientGraph("local:" + f.getAbsolutePath());
        graph.shutdown();

        final Semaphore semaphore = new Semaphore(1);
        semaphore.acquire();

        ExecutorService es1 = Executors.newFixedThreadPool(1);
        Future<Boolean> future1 = es1.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                IndexableGraph graph = new OrientGraph("local:" + f.getAbsolutePath());
                semaphore.acquire();
                Index<Vertex> index = graph.getIndex("testIndex1", Vertex.class);
                CloseableIterable<Vertex> vertexIter = index.get("value", "value1");
                Assert.assertTrue(vertexIter.iterator().hasNext());
                graph.shutdown();
                return true;
            }
        });

        ExecutorService es2 = Executors.newFixedThreadPool(1);
        Future<Boolean> future2 = es2.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                IndexableGraph graph = new OrientGraph("local:" + f.getAbsolutePath());
                Index<Vertex> index = graph.createIndex("testIndex1", Vertex.class);
                Vertex v = graph.addVertex(null);
                index.put("value", "value1", v);
                ((TransactionalGraph) graph).commit();
                graph.shutdown();
                semaphore.release();
                return true;
            }
        });

        future1.get();
    }

    @Test
    public void testIndexAsUsedForSequences() throws IOException, InterruptedException {
        final String url = "/tmp/blueprintstest2";
        File dir = new File(url);
        FileUtils.deleteDirectory(dir);
        final File f = new File(url);

        IndexableGraph graph = new OrientGraph("local:" + f.getAbsolutePath());
        Vertex v1 = graph.addVertex(null);

        final Semaphore semaphore = new Semaphore(1);
        semaphore.acquire();
        ExecutorService es2 = Executors.newFixedThreadPool(1);
        Future<Boolean> future2 = es2.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                IndexableGraph graph = new OrientGraph("local:" + f.getAbsolutePath());
                Index<Edge> indexForSequence = graph.createIndex("indexForSequenceEdge", Edge.class);
                ((TransactionalGraph) graph).commit();
                graph.shutdown();
                semaphore.release();
                return true;
            }
        });

        semaphore.acquire();

        Vertex v2 = graph.addVertex(null);
        OrientIndex<OrientEdge> indexForSequence = (OrientIndex<OrientEdge>) graph.getIndex("indexForSequenceEdge", OrientEdge.class);
        OrientEdge edge = (OrientEdge) graph.addEdge(null, v1, v2, "edge1");
        indexForSequence.put("index", 1, edge);

        CloseableIterable<OrientEdge> iterable = indexForSequence.get("index", 1);
        Assert.assertTrue(iterable.iterator().hasNext());

        ((TransactionalGraph) graph).commit();
        graph.shutdown();

    }

    @Test
    public void testRawIndex() throws IOException {
        final String url = "/tmp/blueprintstest2";
        File dir = new File(url);
        FileUtils.deleteDirectory(dir);
        final File f = new File(url);

        OrientGraph graph = new OrientGraph("local:" + f.getAbsolutePath());
        OGraphDatabase rawGraph = graph.getRawGraph();

        rawGraph.begin(OTransaction.TXTYPE.OPTIMISTIC);

        Assert.assertEquals(OTransaction.TXSTATUS.BEGUN, graph.getRawGraph().getTransaction().getStatus());

        ODocument vertex1 = rawGraph.createVertex();
        ODocument vertex2 = rawGraph.createVertex();
        ODocument edge = rawGraph.createEdge(vertex1, vertex2, "label1");

        OIndexManagerProxy oIndexManagerProxy = graph.getRawGraph().getMetadata().getIndexManager();
        OIndex<Collection<OIdentifiable>> oIndex = (OIndex<Collection<OIdentifiable>>)oIndexManagerProxy.createIndex("indexName", OClass.INDEX_TYPE.NOTUNIQUE.toString(), new OSimpleKeyIndexDefinition(OType.INTEGER), null, null);
        OIndex<Collection<OIdentifiable>> underlying = new OIndexTxAwareMultiValue(graph.getRawGraph(), oIndex);

        underlying.put(1, edge);

        Collection<OIdentifiable> result = underlying.get(1);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(edge, result.iterator().next());

        rawGraph.commit();
    }

    @Test
    public void duplicatetestRawIndexWithBlueprints() throws IOException {
        final String url = "/tmp/blueprintstest2";
        File dir = new File(url);
        FileUtils.deleteDirectory(dir);
        final File f = new File(url);

        OrientGraph graph = new OrientGraph("local:" + f.getAbsolutePath());

        Vertex vertex1 = graph.addVertex(null);
        Vertex vertex2 = graph.addVertex(null);
        Index<Edge> index = graph.createIndex("indexName", Edge.class);
        Edge edge = graph.addEdge(null, vertex1, vertex2, "label1");

        index.put("someKey", 1, edge);

        CloseableIterable<Edge> result = index.get("someKey", 1);
        Assert.assertTrue(result.iterator().hasNext());
        Assert.assertEquals(edge, result.iterator().next());

        graph.commit();


    }

    @Test
    public void testNewInstanceInNewThread() throws IOException, InterruptedException {
        final String url = "/tmp/blueprintstest2";
        File dir = new File(url);
        FileUtils.deleteDirectory(dir);
        final File f = new File(url);

        OrientGraph graph = new OrientGraph("local:" + f.getAbsolutePath());
        Vertex v1 = graph.addVertex(null);

        final Semaphore semaphore = new Semaphore(1);
        semaphore.acquire();

        ExecutorService es1 = Executors.newFixedThreadPool(1);
        Future<Boolean> future1 = es1.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    OrientGraph graph = new OrientGraph("local:" + f.getAbsolutePath());
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

        Assert.assertEquals(2, graph.getRawGraph().countVertexes());
    }



}
