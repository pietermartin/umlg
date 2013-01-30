package org.tuml.blueprints;

import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.index.OIndex;
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
                ((TransactionalGraph)graph).commit();
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
                ((TransactionalGraph)graph).commit();
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

        ((TransactionalGraph)graph).commit();

        OIndex<?> underlying = indexForSequence.getRawIndex();
        Iterator<OIdentifiable> iter = underlying.valuesIterator();
        while (iter.hasNext()) {
             System.out.println(iter.next());
        }
//        Collection<OIdentifiable> records = underlying.getValuesMajor(0, true);
//        Assert.assertTrue(!records.isEmpty());

        graph.shutdown();

    }
}
