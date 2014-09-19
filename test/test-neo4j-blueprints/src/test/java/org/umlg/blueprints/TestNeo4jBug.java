package org.umlg.blueprints;

import com.tinkerpop.gremlin.neo4j.structure.Neo4jGraph;
import com.tinkerpop.gremlin.process.T;
import com.tinkerpop.gremlin.structure.Edge;
import com.tinkerpop.gremlin.structure.Element;
import com.tinkerpop.gremlin.structure.Vertex;
import org.apache.commons.lang.time.StopWatch;
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

        Assert.assertEquals(1, a.outE("LABEL1").count().next(), 0);
        Assert.assertEquals(0, a.outE("LABEL2").count().next(), 0);

    }
}
