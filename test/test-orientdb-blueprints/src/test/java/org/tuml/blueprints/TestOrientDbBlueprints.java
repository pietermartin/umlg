package org.tuml.blueprints;

import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;

/**
 * Date: 2013/01/26
 * Time: 2:53 PM
 */
public class TestOrientDbBlueprints {

    @Test
    public void test() throws IOException, InterruptedException, ExecutionException {
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
                OrientVertex v = (OrientVertex) index.get("value", "value1");
                Assert.assertNotNull(v);
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
}
