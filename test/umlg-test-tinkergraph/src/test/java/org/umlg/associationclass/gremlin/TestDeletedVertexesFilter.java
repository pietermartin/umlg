package org.umlg.associationclass.gremlin;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.pipes.util.Pipeline;
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

        Object vertexes = UMLG.get().executeQuery(UmlgQueryEnum.GROOVY, null, "g.V");
        Assert.assertTrue(vertexes instanceof Pipeline);
        Pipeline<Object, Vertex> pipe = (Pipeline<Object, Vertex>) vertexes;
        Assert.assertEquals(17, pipe.count());

        human4.delete();
        db.commit();

        vertexes = UMLG.get().executeQuery(UmlgQueryEnum.GROOVY, null, "g.V");
        Assert.assertTrue(vertexes instanceof Pipeline);
        pipe = (Pipeline<Object, Vertex>) vertexes;
        Assert.assertEquals(16, pipe.count());

    }
}
