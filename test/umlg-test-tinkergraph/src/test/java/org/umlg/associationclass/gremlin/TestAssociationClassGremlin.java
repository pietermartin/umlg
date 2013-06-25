package org.umlg.associationclass.gremlin;

import junit.framework.Assert;
import org.junit.Test;
import org.umlg.runtime.adaptor.GraphDb;
import org.umlg.runtime.adaptor.TumlQueryEnum;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.tinkergraph.AssociationClassSet;
import org.umlg.tinkergraph.Human;
import org.umlg.tinkergraph.ProjectSet;

/**
 * Date: 2013/06/23
 * Time: 1:50 PM
 */
public class TestAssociationClassGremlin extends BaseLocalDbTest {

    @Test
    public void testGremlinOnAssociationClass() {
        Human human = new Human(true);
        human.setName("human1");
        AssociationClassSet associationClass1 = new AssociationClassSet(true);
        associationClass1.setWeight(1);
        ProjectSet project = new ProjectSet(human, associationClass1);
        project.setName("project1");

        AssociationClassSet associationClass2 = new AssociationClassSet(true);
        associationClass2.setWeight(2);
        ProjectSet project2 = new ProjectSet(human, associationClass2);
        project2.setName("project2");

        AssociationClassSet associationClass3 = new AssociationClassSet(true);
        associationClass3.setWeight(3);
        ProjectSet project3 = new ProjectSet(human, associationClass3);
        project3.setName("project3");

        db.commit();

//        Assert.assertEquals(7, countVertices());
//        Assert.assertEquals(17, countEdges());

        String result = GraphDb.getDb().executeQuery(TumlQueryEnum.GREMLIN, human.getId(), "this.outE.hasU('x')");
        System.out.println(result);

    }
}
