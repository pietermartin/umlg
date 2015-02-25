package org.umlg.associationclass.gremlin;

import org.apache.tinkerpop.gremlin.process.graph.traversal.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.junit.Assert;
import org.junit.Test;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.adaptor.UmlgQueryEnum;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.tinkergraph.Human;

/**
 * Date: 2014/04/21
 * Time: 11:38 AM
 */
public class TestDeletedVertexesFilter extends BaseLocalDbTest {

    @Test
    public void testGremlinOnAssociationClass() {
        Human human1 = new Human(true);
        human1.setName("human1");
        Human human2 = new Human(true);
        human2.setName("human2");
        Human human3 = new Human(true);
        human3.setName("human3");
        Human human4 = new Human(true);
        human4.setName("human4");
        db.commit();

        GraphTraversal<Vertex, Vertex> vertexes = UMLG.get().executeQuery(UmlgQueryEnum.GROOVY, null, "g.V()");
        Assert.assertEquals(6, vertexes.count().next(), 0);

        human4.delete();
        db.commit();

        vertexes = UMLG.get().executeQuery(UmlgQueryEnum.GROOVY, null, "g.V().hasNot('_deleted')");
        Assert.assertEquals(5, vertexes.count().next(), 0);

    }
}
