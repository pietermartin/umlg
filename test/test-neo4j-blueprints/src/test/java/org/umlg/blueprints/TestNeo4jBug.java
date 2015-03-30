package org.umlg.blueprints;

import org.apache.tinkerpop.gremlin.neo4j.structure.Neo4jGraph;
import org.apache.tinkerpop.gremlin.process.traversal.T;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.util.iterator.IteratorUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Date: 2014/07/20
 * Time: 4:54 PM
 */
public class TestNeo4jBug {


    @Test
    public void testVertexToSelfEdgeBug() {
        File f = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "neo4j-selfedgebug");
        if (f.exists()) {
            f.delete();
        }
        f.mkdir();
        Neo4jGraph g = Neo4jGraph.open(f.getAbsolutePath());

        Vertex a = g.addVertex(T.label, "A");
        Vertex b = g.addVertex(T.label, "B");
        a.addEdge("LABEL1", a);
        g.tx().commit();

        Assert.assertEquals(1, IteratorUtils.count(a.edges(Direction.OUT, "LABEL1")), 0);
        Assert.assertEquals(0, IteratorUtils.count(a.edges(Direction.OUT, "LABEL2")), 0);

    }
}
