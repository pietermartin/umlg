package org.umlg.associationclass.gremlin;

import org.junit.Test;
import org.umlg.runtime.adaptor.GraphDb;
import org.umlg.runtime.adaptor.UmlgQueryEnum;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.tinkergraph.AssociationClassSetTest;
import org.umlg.tinkergraph.Human;
import org.umlg.tinkergraph.ProjectSetTest;

/**
 * Date: 2013/06/23
 * Time: 1:50 PM
 */
public class TestAssociationClassGremlin extends BaseLocalDbTest {

    @Test
    public void testGremlinOnAssociationClass() {
        Human human = new Human(true);
        human.setName("human1");
        AssociationClassSetTest associationClass1 = new AssociationClassSetTest(true);
        associationClass1.setWeight(1);
        ProjectSetTest project = new ProjectSetTest(human, associationClass1);
        project.setName("project1");

        AssociationClassSetTest associationClass2 = new AssociationClassSetTest(true);
        associationClass2.setWeight(2);
        ProjectSetTest project2 = new ProjectSetTest(human, associationClass2);
        project2.setName("project2");

        AssociationClassSetTest associationClass3 = new AssociationClassSetTest(true);
        associationClass3.setWeight(3);
        ProjectSetTest project3 = new ProjectSetTest(human, associationClass3);
        project3.setName("project3");

        db.commit();

//        Assert.assertEquals(7, countVertices());
//        Assert.assertEquals(17, countEdges());

        String result = GraphDb.getDb().executeQuery(UmlgQueryEnum.GREMLIN, human.getId(), "self.outE.hasU('xlkjljlkjy')");
        System.out.println(result);

    }
}
