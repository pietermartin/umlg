package org.umlg.titan.test;

import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

/**
 * Date: 2013/01/26
 * Time: 2:53 PM
 */
public class TestTitanBlueprints {

    @Test
    public void testIndexCreatedInAThreadUsedInAnother() throws IOException, InterruptedException, ExecutionException {
        final String url = System.getProperty("java.io.tmpdir") + System.lineSeparator() + "titan-test";
        File dir = new File(url);
        FileUtils.deleteDirectory(dir);
        final File f = new File(url);
        Configuration propertiesConfiguration = new PropertiesConfiguration();
        propertiesConfiguration.addProperty("storage.backend", "persistit");
        propertiesConfiguration.addProperty("storage.directory", f.getAbsolutePath());
        TitanGraph g = TitanFactory.open(propertiesConfiguration);

        //Create the index
        g.createKeyIndex("testKeyIndexonEdge", Edge.class);

        Vertex juno = g.addVertex(null);
        juno.setProperty("name", "juno");
        Vertex jupiter = g.addVertex(null);
        jupiter.setProperty("name", "jupiter");
        Edge married = g.addEdge(null, juno, jupiter, "married");
        //Index a value
        married.setProperty("testKeyIndexonEdge", "specialValue");
        g.commit();

        married = g.getEdge(married.getId());
        g.removeEdge(married);
        g.commit();

    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmailList() throws Exception {

        final String url = System.getProperty("java.io.tmpdir") + System.lineSeparator() + "titan-test";
        File dir = new File(url);
        FileUtils.deleteDirectory(dir);
        final File f = new File(url);
        Configuration propertiesConfiguration = new PropertiesConfiguration();
        propertiesConfiguration.addProperty("storage.backend", "local");
        propertiesConfiguration.addProperty("storage.directory", f.getAbsolutePath());
        TitanGraph g = TitanFactory.open(propertiesConfiguration);

        Vertex v1 = g.addVertex(null);
        Vertex v2 = g.addVertex(null);
        v2.setProperty("emailList", "jj@jj.jj");
        g.addEdge(null, v1, v2, "emailList");
        g.commit();

    }

    @Test
    public void testStringArray() throws Exception {

        final String url = System.getProperty("java.io.tmpdir") + System.lineSeparator() + "titan-test";
        File dir = new File(url);
        FileUtils.deleteDirectory(dir);
        final File f = new File(url);
        Configuration propertiesConfiguration = new PropertiesConfiguration();
        propertiesConfiguration.addProperty("storage.backend", "persistit");
        propertiesConfiguration.addProperty("storage.directory", f.getAbsolutePath());
        TitanGraph g = TitanFactory.open(propertiesConfiguration);

        Vertex v1 = g.addVertex(null);
        Vertex v2 = g.addVertex(null);
        v2.setProperty("stringArray1", new String[]{"a", "b", "c"});
        g.addEdge(null, v1, v2, "emailList");
        g.commit();

        Vertex v21 = g.getVertex(v2.getId());
        String[] s = v21.getProperty("stringArray1");
        Assert.assertArrayEquals(new String[]{"a", "b", "c"}, s);

        g.commit();

    }

    @Test
    public void testForumQuestion() throws Exception {
        final String url = System.getProperty("java.io.tmpdir") + System.lineSeparator() + "titan-test";
        File dir = new File(url);
        FileUtils.deleteDirectory(dir);
        final File f = new File(url);
        Configuration propertiesConfiguration = new PropertiesConfiguration();
        propertiesConfiguration.addProperty("storage.backend", "persistit");
        propertiesConfiguration.addProperty("storage.directory", f.getAbsolutePath());
        TitanGraph g = TitanFactory.open(propertiesConfiguration);

        Vertex nodeA = g.addVertex(null);
        Vertex aliasNode;
        Iterator<Vertex> aItr = g.query().has("aliasName", "person").vertices().iterator();
        if (aItr.hasNext()){
            aliasNode = aItr.next();
        }else{
            aliasNode = g.addVertex(null);
            aliasNode.setProperty("aliasName", "person");
        }
        aliasNode.addEdge("alias", nodeA);
        g.commit();
        Assert.assertTrue(g.query().has("aliasName", "person").vertices().iterator().hasNext());
        g.shutdown();
    }

    @Test
    public void testEmptyLabels() throws Exception {
        final String url = System.getProperty("java.io.tmpdir") + System.lineSeparator() + "titan-test";
        File dir = new File(url);
        FileUtils.deleteDirectory(dir);
        final File f = new File(url);
        Configuration propertiesConfiguration = new PropertiesConfiguration();
        propertiesConfiguration.addProperty("storage.backend", "persistit");
        propertiesConfiguration.addProperty("storage.directory", f.getAbsolutePath());
        TitanGraph g = TitanFactory.open(propertiesConfiguration);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Vertex v1 = g.addVertex(null);
            v1.setProperty("number", -1);
            for (int i = 0; i < 10000; i++) {
                Vertex v2 = g.addVertex(null);
                v1.setProperty("number", i);
                g.addEdge(null, v2, v1, "test1");
                g.addEdge(null, v2, v1, "test2");
            }

            Assert.assertEquals(20000, count(g.getVertex(4L).getVertices(Direction.IN)));
            Assert.assertEquals(10000, count(g.getVertex(4L).getVertices(Direction.IN, "test1")));
            Assert.assertEquals(10000, count(g.getVertex(4L).getVertices(Direction.IN, "test2")));
            Assert.assertEquals(20000, count(g.getVertex(4L).getVertices(Direction.IN, "test1", "test2")));

            g.commit();

            for (Vertex v : g.getVertex(4L).getVertices(Direction.IN, "test1")) {
                Assert.assertEquals(1, count(v.getEdges(Direction.OUT, "test1")));
                v.getEdges(Direction.OUT, "test1").iterator().next().remove();
            }
            Assert.assertEquals(10000, count(g.getVertex(4L).getVertices(Direction.IN)));
            Assert.assertEquals(0, count(g.getVertex(4L).getVertices(Direction.IN, "test1")));
            Assert.assertEquals(10000, count(g.getVertex(4L).getVertices(Direction.IN, "test2")));
            Assert.assertEquals(10000, count(g.getVertex(4L).getVertices(Direction.IN, "test1", "test2")));

        } finally {
            stopWatch.stop();
            System.out.print(stopWatch.toString());
            g.shutdown();
        }
    }

//    @Test
    public void testRemoveTransactionAlreadyWritable() throws IOException {
        final String url = System.getProperty("java.io.tmpdir") + System.lineSeparator() + "titan-test";
        File dir = new File(url);
        FileUtils.deleteDirectory(dir);
        final File f = new File(url);
        Configuration propertiesConfiguration = new PropertiesConfiguration();
        propertiesConfiguration.addProperty("storage.backend", "persistit");
        propertiesConfiguration.addProperty("storage.directory", f.getAbsolutePath());
        TitanGraph graph = TitanFactory.open(propertiesConfiguration);
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

            Assert.assertEquals(11, count(graph.getVertices()));
            Assert.assertEquals(1, count(graph.getVertices("name", "aaaa")));
            Assert.assertEquals(10, count(graph.getVertices("name", "bbbb")));
            Assert.assertEquals(10, count(graph.getEdges("name", "cccc")));

            Iterator<Vertex> iter = graph.getVertices("name", "bbbb").iterator();
            Vertex v = iter.next();
            long indexToRemove = (Long)v.getId();
            v.setProperty("name", "bbbba");
            Assert.assertEquals(indexToRemove, v.getId());
            Assert.assertEquals("bbbba", v.getProperty("name"));
            iter.remove();
            Assert.assertNull(graph.getVertex(1L));
            Assert.assertEquals(9, count(iter));

            graph.commit();
            iter = graph.getVertices("name", "bbbb").iterator();
            Assert.assertEquals(9, count(iter));

        } finally {
            graph.shutdown();
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
